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

  public String generateToken(UUID userId, String username, String email) {
    return Jwts.builder()
        .setSubject(userId.toString())
        .claim("username", username)
        .claim("email", email)
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

  public String extractUsername(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .get("username", String.class);
  }

  public String extractEmail(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .get("email", String.class);
  }

  public UUID extractUserIdAllowExpired(String token) {
    Claims claims = extractAllClaimsAllowExpired(token);
    return UUID.fromString(claims.getSubject());
  }

  public String extractUsernameAllowExpired(String token) {
    Claims claims = extractAllClaimsAllowExpired(token);
    return claims.get("username", String.class);
  }

  public String extractEmailAllowExpired(String token) {
    Claims claims = extractAllClaimsAllowExpired(token);
    return claims.get("email", String.class);
  }

  private Claims extractAllClaimsAllowExpired(String token) {
    try {
      return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }
}
