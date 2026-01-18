package com.drawduel.application.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

public class JwtService {
  private final Key key;
  private final Long expirationMs;

  public JwtService(String secret, Long expiration) {
    this.key = io.jsonwebtoken.security.Keys.hmacShaKeyFor(secret.getBytes());
    this.expirationMs = expiration;
  }

  public String generateRefreshToken() {
    SecureRandom random = new SecureRandom();

    byte[] bytes = new byte[64];
    random.nextBytes(bytes);

    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  }

  public String generateToken(UUID userId, UUID sessionId) {
    return Jwts.builder()
        .setSubject(userId.toString())
        .claim("sessionId", sessionId.toString())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
        .signWith(key)
        .compact();
  }

  public boolean isTokenValid(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (JwtException e) {
      return false;
    }
  }

  public UUID extractUserId(String token) {
    String userId =
        Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    return UUID.fromString(userId);
  }

  public UUID extractUserIdAllowExpired(String token) {
    Claims claims = extractAllClaimsAllowExpired(token);
    return UUID.fromString(claims.getSubject());
  }

  public UUID extractSessionIdAllowExpired(String token) {
    Claims claims = extractAllClaimsAllowExpired(token);
    String sessionId = claims.get("sessionId", String.class);
    return UUID.fromString(sessionId);
  }

  private Claims extractAllClaimsAllowExpired(String token) {
    try {
      return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }
}
