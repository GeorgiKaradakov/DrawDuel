package com.drawduel.infrastructure.persistence.websocket;

import com.drawduel.application.dtos.GameDto;
import com.drawduel.application.ports.CreateGameUseCasePort;
import com.drawduel.application.ports.UpdateGameUseCasePort;
import com.drawduel.application.services.JwtService;
import com.drawduel.domain.models.Round;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@RequiredArgsConstructor
public class DrawDuelWebSocketHandler extends TextWebSocketHandler {

  private static final long ROUND_TIMEOUT_SECONDS = 60;
  private static final long WORD_CHOICE_TIMEOUT_SECONDS = 10;

  private final JwtService jwtService;
  private final CreateGameUseCasePort createGameUseCase;
  private final UpdateGameUseCasePort updateGameUseCase;
  private final ObjectMapper mapper = new ObjectMapper();

  private final Queue<UUID> waitingPlayers = new ConcurrentLinkedQueue<>();
  private final Map<UUID, WebSocketSession> playerSessions = new ConcurrentHashMap<>();
  private final Map<WebSocketSession, UUID> sessionPlayers = new ConcurrentHashMap<>();
  private final Map<String, Round> activeRounds = new ConcurrentHashMap<>();
  private final Map<String, GameDto> activeGames = new ConcurrentHashMap<>();
  private final Map<UUID, String> usernames = new ConcurrentHashMap<>();

  private static final List<String> WORD_POOL =
      List.of("apple", "house", "dog", "car", "computer", "sun", "banana", "tree", "cat", "phone");

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
      case "timeUpRound" -> {
        UUID playerId = sessionPlayers.get(session);
        Round round =
            activeRounds.values().stream()
                .filter(r -> r.getDrawerId().equals(playerId) || r.getGuesserId().equals(playerId))
                .findFirst()
                .orElse(null);
        if (round != null) handleRoundTimeout(round);
      }
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

    System.out.println("Session hash: " + session.hashCode());
    boolean player1IsDrawer = new Random().nextBoolean();
    UUID drawerId = player1IsDrawer ? playerId : waiting;
    UUID guesserId = player1IsDrawer ? waiting : playerId;

    GameDto game =
        createGameUseCase.handle(new CreateGameUseCasePort.Query(drawerId, guesserId)).gameDto();
    activeGames.put(game.id().toString(), game);

    startNewRound(game, drawerId, guesserId, 1);
  }

  private void startNewRound(GameDto game, UUID drawerId, UUID guesserId, int roundNumber)
      throws IOException {
    Round round =
        new Round(UUID.fromString(game.id().toString()), drawerId, guesserId, roundNumber);
    activeRounds.put(game.id().toString(), round);

    List<String> randomWords = pickRandomWords(3);

    if (roundNumber == 1) {
      sendToPlayer(drawerId, Map.of("type", "sendToDraw", "gameId", game.id().toString()));
      sendToPlayer(guesserId, Map.of("type", "sendToGuess", "gameId", game.id().toString()));
    }

    sendToPlayer(
        drawerId,
        Map.of(
            "type",
            "wordChoice",
            "gameId",
            game.id().toString(),
            "role",
            "drawer",
            "words",
            randomWords,
            "countdown",
            WORD_CHOICE_TIMEOUT_SECONDS,
            "round",
            roundNumber));

    sendToPlayer(
        guesserId,
        Map.of(
            "type",
            "waitingForWord",
            "gameId",
            game.id().toString(),
            "role",
            "guesser",
            "message",
            "Waiting for drawer to choose a word...",
            "countdown",
            WORD_CHOICE_TIMEOUT_SECONDS,
            "round",
            roundNumber));

    System.out.printf(
        "Round %d started | Drawer: %s | Guesser: %s%n", roundNumber, drawerId, guesserId);
  }

  private void handleRoundTimeout(Round round) throws IOException {
    System.out.println("Round time expired for game " + round.getId());

    Map<String, Integer> scores =
        calculateRoundPoints(0, (int) ROUND_TIMEOUT_SECONDS, round.getGuessCount());
    processRoundEnd(round, round.getDrawerId(), round.getGuesserId(), scores);
  }

  private void handleWordChoice(WebSocketSession session, String chosenWord) throws IOException {
    UUID drawerId = sessionPlayers.get(session);
    Round round = findRoundByDrawer(drawerId);
    if (round == null) return;

    round.setChosenWord(chosenWord);

    sendToPlayer(
        drawerId,
        Map.of(
            "type",
            "startRound",
            "gameId",
            round.getId().toString(),
            "role",
            "drawer",
            "word",
            chosenWord,
            "countdown",
            ROUND_TIMEOUT_SECONDS,
            "round",
            round.getRoundNumber()));

    sendToPlayer(
        round.getGuesserId(),
        Map.of(
            "type",
            "startRound",
            "gameId",
            round.getId().toString(),
            "role",
            "guesser",
            "wordLength",
            chosenWord.length(),
            "countdown",
            ROUND_TIMEOUT_SECONDS,
            "round",
            round.getRoundNumber()));
  }

  private void handleWordChoiceTimeout(WebSocketSession session, JsonNode wordsNode)
      throws IOException {
    if (wordsNode == null || !wordsNode.isArray() || wordsNode.isEmpty()) return;
    List<String> offered = new ArrayList<>();
    wordsNode.forEach(n -> offered.add(n.asText()));
    String randomWord = offered.get(new Random().nextInt(offered.size()));
    handleWordChoice(session, randomWord);
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
    Round round = findRoundByGuesser(guesserId);
    if (round == null || round.getChosenWord() == null) return;

    boolean correct = guess.equalsIgnoreCase(round.getChosenWord());
    round.incrementGuessCount();
    String playerName = usernames.getOrDefault(guesserId, "Unknown");

    sendToBothPlayers(
        round.getDrawerId(),
        guesserId,
        Map.of(
            "type", "guessMessage", "guess", guess, "correct", correct, "playerName", playerName));

    if (correct) {
      Map<String, Integer> scores =
          calculateRoundPoints(timeLeft, (int) ROUND_TIMEOUT_SECONDS, round.getGuessCount());
      processRoundEnd(round, round.getDrawerId(), guesserId, scores);
    }
  }

  private void processRoundEnd(
      Round round, UUID drawerId, UUID guesserId, Map<String, Integer> scores) throws IOException {
    GameDto game = activeGames.get(round.getId().toString());
    boolean drawerIsPlayerA = game.playerAId().equals(drawerId);

    GameDto updatedGame =
        updateGameUseCase
            .handle(
                new UpdateGameUseCasePort.Query(
                    round.getId(),
                    scores.get("drawerPoints"),
                    scores.get("guesserPoints"),
                    drawerIsPlayerA))
            .gameDto();
    activeGames.put(updatedGame.id().toString(), updatedGame);

    sendToBothPlayers(
        drawerId,
        guesserId,
        Map.of(
            "type",
            "roundEnd",
            "drawerName",
            usernames.get(drawerId),
            "guesserName",
            usernames.get(guesserId),
            "drawerScore",
            scores.get("drawerPoints"),
            "guesserScore",
            scores.get("guesserPoints")));

    int nextRound = round.getRoundNumber() + 1;
    if (nextRound <= updatedGame.totalRounds()) {
      activeRounds.remove(round.getId().toString());
      new Timer()
          .schedule(
              new TimerTask() {
                public void run() {
                  try {
                    startNewRound(updatedGame, guesserId, drawerId, nextRound);
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                }
              },
              5500);
    } else {
      activeRounds.remove(round.getId().toString());
      activeGames.remove(game.id().toString());
      endGame(updatedGame);
    }
  }

  private void endGame(GameDto game) throws IOException {
    int playerATotal = game.playerADrawPoints() + game.playerAGuessPoints();
    int playerBTotal = game.playerBDrawPoints() + game.playerBGuessPoints();

    UUID winner =
        playerATotal == playerBTotal
            ? null
            : (playerATotal > playerBTotal ? game.playerAId() : game.playerBId());

    sendToPlayer(
        game.playerAId(),
        Map.of(
            "type",
            "gameEnd",
            "playerAName",
            usernames.get(game.playerAId()),
            "playerBName",
            usernames.get(game.playerBId()),
            "playerAScore",
            playerATotal,
            "playerBScore",
            playerBTotal,
            "playerId",
            game.playerAId().toString(),
            "winner",
            winner == null ? "Draw" : usernames.get(winner)));

    sendToPlayer(
        game.playerBId(),
        Map.of(
            "type",
            "gameEnd",
            "playerAName",
            usernames.get(game.playerAId()),
            "playerBName",
            usernames.get(game.playerBId()),
            "playerAScore",
            playerATotal,
            "playerBScore",
            playerBTotal,
            "playerId",
            game.playerBId().toString(),
            "winner",
            winner == null ? "Draw" : usernames.get(winner)));

    System.out.printf(
        "Game %s ended — Winner: %s (%d:%d)%n",
        game.id(), winner == null ? "Draw" : usernames.get(winner), playerATotal, playerBTotal);
  }

  private Round findRoundByDrawer(UUID drawerId) {
    return activeRounds.values().stream()
        .filter(r -> r.getDrawerId().equals(drawerId))
        .findFirst()
        .orElse(null);
  }

  private Round findRoundByGuesser(UUID guesserId) {
    return activeRounds.values().stream()
        .filter(r -> r.getGuesserId().equals(guesserId))
        .findFirst()
        .orElse(null);
  }

  private UUID getOpponent(UUID playerId) {
    return activeRounds.values().stream()
        .filter(r -> r.getDrawerId().equals(playerId) || r.getGuesserId().equals(playerId))
        .map(r -> r.getDrawerId().equals(playerId) ? r.getGuesserId() : r.getDrawerId())
        .findFirst()
        .orElse(null);
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

  private List<String> pickRandomWords(int count) {
    List<String> shuffled = new ArrayList<>(WORD_POOL);
    Collections.shuffle(shuffled);
    return shuffled.subList(0, Math.min(count, shuffled.size()));
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
