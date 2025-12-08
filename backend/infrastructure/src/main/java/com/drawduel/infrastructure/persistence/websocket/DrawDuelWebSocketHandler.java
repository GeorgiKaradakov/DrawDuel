package com.drawduel.infrastructure.persistence.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class DrawDuelWebSocketHandler extends TextWebSocketHandler {
  public record WsMessage(String type, Object data) {}

  private final Queue<WebSocketSession> waitingPlayers = new ConcurrentLinkedQueue<>();
  private final Map<WebSocketSession, WebSocketSession> pairs = new ConcurrentHashMap<>();
  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    System.out.println("Player connected: " + session.getId());
    matchPlayers(session);
  }

  private void matchPlayers(WebSocketSession newPlayer) throws IOException {
    WebSocketSession waiting = waitingPlayers.poll();

    if (waiting == null) {
      waitingPlayers.add(newPlayer);
      newPlayer.sendMessage(
          new TextMessage(
              mapper.writeValueAsString(
                  Map.of(
                      new WsMessage("type", "waiting"),
                      new WsMessage("waiting", "Waiting for opponent...")))));
    } else {
      pairs.put(newPlayer, waiting);
      pairs.put(waiting, newPlayer);

      waiting.sendMessage(
          new TextMessage(
              mapper.writeValueAsString(
                  Map.of(
                      new WsMessage("type", "start"),
                      new WsMessage("waiting", "Opponent Found!")))));
      newPlayer.sendMessage(
          new TextMessage(
              mapper.writeValueAsString(
                  Map.of(
                      new WsMessage("type", "start"),
                      new WsMessage("waiting", "Opponent Found!")))));

      System.out.println("Matched players: " + newPlayer.getId() + " <-> " + waiting.getId());
    }
  }

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    WebSocketSession opponent = pairs.get(session);

    if (opponent != null && opponent.isOpen()) {
      opponent.sendMessage(message);
    } else {
      session.sendMessage(
          new TextMessage(
              mapper.writeValueAsString(
                  Map.of(
                      new WsMessage("type", "error"),
                      new WsMessage("message", "No opponent connected.")))));
    }
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    WebSocketSession opponent = pairs.remove(session);

    if (opponent != null) {
      pairs.remove(opponent);
      if (opponent.isOpen()) {
        opponent.sendMessage(
            new TextMessage(
                mapper.writeValueAsString(
                    Map.of(
                        new WsMessage("type", "disconnect"),
                        new WsMessage("message", "Opponent left!")))));
        opponent.close();
      }
    }

    waitingPlayers.remove(session);
    System.out.println("Player disconnected: " + session.getId());
  }
}
