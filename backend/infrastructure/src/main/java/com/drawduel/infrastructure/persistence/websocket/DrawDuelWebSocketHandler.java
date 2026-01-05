package com.drawduel.infrastructure.persistence.websocket;

import com.drawduel.application.services.JwtService;
import com.drawduel.domain.enums.GameStatus;
import com.drawduel.infrastructure.persistence.jpa.entities.JpaGameEntity;
import com.drawduel.infrastructure.persistence.jpa.repositories.GameJpaRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class DrawDuelWebSocketHandler extends TextWebSocketHandler {

  private final JwtService jwtService;
  private final GameJpaRepository gameRepository;
  private final ObjectMapper mapper = new ObjectMapper();

  private final Queue<UUID> waitingPlayers = new ConcurrentLinkedQueue<>();
  private final Map<UUID, WebSocketSession> playerSessions = new ConcurrentHashMap<>();
  private final Map<WebSocketSession, UUID> sessionPlayers = new ConcurrentHashMap<>();
  private final Map<String, GameRoom> games = new ConcurrentHashMap<>();
  private final Map<UUID, String> usernames = new ConcurrentHashMap<>();

  private record GameRoom(
      String gameId, UUID drawerId, UUID guesserId, String chosenWord, int roundNumber) {}

  private static final List<String> WORD_POOL =
      List.of("apple", "house", "dog", "car", "computer", "sun", "banana", "tree", "cat", "phone");

  public DrawDuelWebSocketHandler(JwtService jwtService, GameJpaRepository gameRepository) {
    this.jwtService = jwtService;
    this.gameRepository = gameRepository;
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws IOException {
    String query = session.getUri().getQuery();
    String token = (query != null && query.contains("token=")) ? query.split("token=")[1] : null;

    if (token == null) {
      session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Missing token"));
      return;
    }

    try {
      String username = jwtService.extractUsernameAllowExpired(token);
      UUID userId = jwtService.extractUserIdAllowExpired(token);
      playerSessions.put(userId, session);
      sessionPlayers.put(session, userId);
      usernames.put(userId, username);
      System.out.println("Connected: " + username + " (" + userId + ")");
    } catch (Exception e) {
      System.out.println("Invalid JWT for WebSocket connection");
      session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Invalid token"));
    }
  }

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    JsonNode node = mapper.readTree(message.getPayload());
    String type = node.get("type").asText();

    switch (type) {
      case "search" -> handlePlayerSearch(UUID.fromString(node.get("userId").asText()), session);
      case "chooseWord" -> handleWordChoice(session, node.get("word").asText());
      case "timeUpWordChoice" -> handleWordChoiceTimeout(session, node.get("words"));
      case "draw" -> handleDraw(session, message);
      case "guess" -> handleGuess(session, node);
    }
  }

  private void handlePlayerSearch(UUID playerId, WebSocketSession session) throws IOException {
    playerSessions.put(playerId, session);
    sessionPlayers.put(session, playerId);

    UUID waiting = waitingPlayers.poll();
    if (waiting == null) {
      waitingPlayers.add(playerId);
      session.sendMessage(
          new TextMessage("{\"type\":\"waiting\",\"message\":\"Waiting for opponent...\"}"));
      return;
    }

    UUID gameId = UUID.randomUUID();
    boolean player1IsDrawer = new Random().nextBoolean();
    UUID drawerId = player1IsDrawer ? playerId : waiting;
    UUID guesserId = player1IsDrawer ? waiting : playerId;

    JpaGameEntity gameEntity = new JpaGameEntity();
    gameEntity.setId(gameId);
    gameEntity.setPlayerAId(drawerId);
    gameEntity.setPlayerBId(guesserId);
    gameEntity.setStatus(GameStatus.IN_PROGRESS);
    gameRepository.save(gameEntity);

    startNewRound(gameEntity, drawerId, guesserId, 1);
  }

  private void startNewRound(
      JpaGameEntity gameEntity, UUID drawerId, UUID guesserId, int roundNumber) throws IOException {
    GameRoom room =
        new GameRoom(gameEntity.getId().toString(), drawerId, guesserId, null, roundNumber);
    games.put(room.gameId(), room);

    List<String> randomWords = pickRandomWords(3);

    if (roundNumber <= 1) {
      sendToPlayer(drawerId, Map.of("type", "sendToDraw", "gameId", room.gameId()));

      sendToPlayer(guesserId, Map.of("type", "sendToGuess", "gameId", room.gameId()));
    }

    sendToPlayer(
        drawerId,
        Map.of(
            "type",
            "wordChoice",
            "gameId",
            room.gameId(),
            "role",
            "drawer",
            "words",
            randomWords,
            "countdown",
            10,
            "round",
            roundNumber));

    sendToPlayer(
        guesserId,
        Map.of(
            "type",
            "waitingForWord",
            "gameId",
            room.gameId(),
            "role",
            "guesser",
            "message",
            "Waiting for drawer to choose a word...",
            "countdown",
            10,
            "round",
            roundNumber));

    System.out.printf(
        "Round %d started | Drawer: %s | Guesser: %s%n", roundNumber, drawerId, guesserId);
  }

  private void handleWordChoice(WebSocketSession session, String chosenWord) throws IOException {
    UUID drawerId = sessionPlayers.get(session);
    UUID guesserId = getOpponent(drawerId);
    if (guesserId == null) return;

    GameRoom room =
        games.values().stream().filter(g -> g.drawerId.equals(drawerId)).findFirst().orElse(null);
    if (room == null) return;

    GameRoom updated =
        new GameRoom(
            room.gameId(), room.drawerId(), room.guesserId(), chosenWord, room.roundNumber());
    games.put(room.gameId(), updated);

    sendToPlayer(
        drawerId,
        Map.of(
            "type",
            "startRound",
            "gameId",
            room.gameId(),
            "role",
            "drawer",
            "word",
            chosenWord,
            "countdown",
            60,
            "round",
            room.roundNumber()));

    sendToPlayer(
        guesserId,
        Map.of(
            "type",
            "startRound",
            "gameId",
            room.gameId(),
            "role",
            "guesser",
            "wordLength",
            chosenWord.length(),
            "countdown",
            60,
            "round",
            room.roundNumber()));

    System.out.println("Drawer chose word '" + chosenWord + "' for game " + room.gameId());
  }

  private void handleWordChoiceTimeout(WebSocketSession session, JsonNode wordsNode)
      throws IOException {
    if (wordsNode == null || !wordsNode.isArray() || wordsNode.isEmpty()) return;
    List<String> offered = new ArrayList<>();
    wordsNode.forEach(n -> offered.add(n.asText()));
    String randomWord = offered.get(new Random().nextInt(offered.size()));
    System.out.println("Drawer timed out — randomly picked: " + randomWord);
    handleWordChoice(session, randomWord);
  }

  private List<String> pickRandomWords(int count) {
    List<String> shuffled = new ArrayList<>(WORD_POOL);
    Collections.shuffle(shuffled);
    return shuffled.subList(0, Math.min(count, shuffled.size()));
  }

  private void handleDraw(WebSocketSession session, TextMessage message) throws IOException {
    UUID playerId = sessionPlayers.get(session);
    UUID opponentId = getOpponent(playerId);
    if (opponentId == null) return;
    WebSocketSession opponentSession = playerSessions.get(opponentId);
    if (opponentSession != null && opponentSession.isOpen()) opponentSession.sendMessage(message);
  }

  private void handleGuess(WebSocketSession session, JsonNode node) throws IOException {
    String guess = node.get("guess").asText();
    int timeLeft = node.get("timeLeft").asInt();
    int guessCount = node.get("guessCount").asInt();

    UUID guesserId = sessionPlayers.get(session);
    UUID drawerId = getOpponent(guesserId);
    if (drawerId == null) return;

    GameRoom room =
        games.values().stream().filter(g -> g.guesserId.equals(guesserId)).findFirst().orElse(null);
    if (room == null || room.chosenWord() == null) return;

    boolean correct = guess.equalsIgnoreCase(room.chosenWord());
    String playerName = usernames.getOrDefault(guesserId, "Unknown");

    sendToBothPlayers(
        drawerId,
        guesserId,
        Map.of(
            "type", "guessMessage",
            "guess", guess,
            "correct", correct,
            "playerName", playerName));

    if (correct) {
      Map<String, Integer> scores = calculateRoundPoints(timeLeft, 60, guessCount);
      processRoundEnd(room, drawerId, guesserId, scores);
    }
  }

  private void processRoundEnd(
      GameRoom room, UUID drawerId, UUID guesserId, Map<String, Integer> scores)
      throws IOException {
    UUID gameId = UUID.fromString(room.gameId());
    JpaGameEntity gameEntity = gameRepository.findById(gameId).orElseThrow();

    boolean drawerIsPlayerA = gameEntity.getPlayerAId().equals(drawerId);
    gameEntity.addScores(scores.get("drawerPoints"), scores.get("guesserPoints"), drawerIsPlayerA);

    int nextRound = room.roundNumber() + 1;
    gameEntity.setCurrentRound(nextRound);
    gameRepository.save(gameEntity);

    sendToBothPlayers(
        drawerId,
        guesserId,
        Map.of(
            "type", "roundEnd",
            "drawerName", usernames.get(drawerId),
            "guesserName", usernames.get(guesserId),
            "drawerScore", scores.get("drawerPoints"),
            "guesserScore", scores.get("guesserPoints")));

    System.out.printf(
        "Round %d ended | Drawer: %d | Guesser: %d%n",
        room.roundNumber(), scores.get("drawerPoints"), scores.get("guesserPoints"));

    if (nextRound <= gameEntity.getTotalRounds()) {
      new Timer()
          .schedule(
              new TimerTask() {
                @Override
                public void run() {
                  try {
                    startNewRound(gameEntity, guesserId, drawerId, nextRound);
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                }
              },
              5500);
    } else {
      endGame(gameEntity);
    }
  }

  private void endGame(JpaGameEntity gameEntity) throws IOException {
    gameEntity.finish();
    int playerATotal = gameEntity.getPlayerADrawPoints() + gameEntity.getPlayerAGuessPoints();
    int playerBTotal = gameEntity.getPlayerBDrawPoints() + gameEntity.getPlayerBGuessPoints();

    UUID winner =
        playerATotal == playerBTotal
            ? null
            : (playerATotal > playerBTotal ? gameEntity.getPlayerAId() : gameEntity.getPlayerBId());
    gameEntity.setWinnerId(winner);
    gameRepository.save(gameEntity);

    sendToBothPlayers(
        gameEntity.getPlayerAId(),
        gameEntity.getPlayerBId(),
        Map.of(
            "type",
            "gameEnd",
            "playerAName",
            usernames.get(gameEntity.getPlayerAId()),
            "playerBName",
            usernames.get(gameEntity.getPlayerBId()),
            "playerAScore",
            playerATotal,
            "playerBScore",
            playerBTotal,
            "winner",
            winner == null ? "Draw" : usernames.get(winner)));

    System.out.printf(
        "Game %s ended — Winner: %s (%d:%d)%n",
        gameEntity.getId(),
        winner == null ? "Draw" : usernames.get(winner),
        playerATotal,
        playerBTotal);
  }

  private void sendToPlayer(UUID playerId, Map<String, Object> payload) throws IOException {
    WebSocketSession session = playerSessions.get(playerId);
    if (session != null && session.isOpen())
      session.sendMessage(new TextMessage(mapper.writeValueAsString(payload)));
  }

  private void sendToBothPlayers(UUID p1, UUID p2, Map<String, Object> payload) throws IOException {
    String json = mapper.writeValueAsString(payload);
    for (UUID id : List.of(p1, p2)) {
      WebSocketSession s = playerSessions.get(id);
      if (s != null && s.isOpen()) s.sendMessage(new TextMessage(json));
    }
  }

  private UUID getOpponent(UUID playerId) {
    return games.values().stream()
        .filter(g -> g.drawerId.equals(playerId) || g.guesserId.equals(playerId))
        .map(g -> g.drawerId.equals(playerId) ? g.guesserId : g.drawerId)
        .findFirst()
        .orElse(null);
  }

  private Map<String, Integer> calculateRoundPoints(int timeLeft, int totalTime, int guessCount) {
    int drawerPoints = (int) (60 + ((double) timeLeft / totalTime) * 40 - (guessCount * 2));
    int guesserPoints = (int) (40 + ((double) timeLeft / totalTime) * 60 - (guessCount * 3));
    return Map.of(
        "drawerPoints", Math.max(0, drawerPoints), "guesserPoints", Math.max(0, guesserPoints));
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    UUID playerId = sessionPlayers.remove(session);
    if (playerId != null) {
      playerSessions.remove(playerId);
      waitingPlayers.remove(playerId);
      System.out.println("Player disconnected: " + playerId);
    }
  }
}
