package com.drawduel.infrastructure.config;

import com.drawduel.infrastructure.persistence.websocket.DrawDuelWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

  private final DrawDuelWebSocketHandler drawDuelSocketHandler;

  public WebSocketConfig(DrawDuelWebSocketHandler drawDuelSocketHandler) {
    this.drawDuelSocketHandler = drawDuelSocketHandler;
  }

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(drawDuelSocketHandler, "/ws/drawduel").setAllowedOrigins("*");
  }
}
