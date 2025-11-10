package com.drawduel.infrastructure.api.ExceptionHandling;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiExceptionHandler {

  // 400 -> invalid input
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
    return plain(HttpStatus.BAD_REQUEST, msg(ex, "Bad Request"));
  }

  // 409 -> conflicts
  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<String> handleIllegalState(IllegalStateException ex) {
    return plain(HttpStatus.CONFLICT, msg(ex, "Conflict"));
  }

  // 400 -> malformed Json body
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<String> handleMalformedJson(HttpMessageNotReadableException ex) {
    return plain(HttpStatus.BAD_REQUEST, msg(ex, "Malformed JSON"));
  }

  // 500 -> everything else
  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGenericExceptions(Exception ex) {
    return plain(HttpStatus.INTERNAL_SERVER_ERROR, msg(ex, "Unexpected Error"));
  }

  private ResponseEntity<String> plain(HttpStatus status, String body) {
    return ResponseEntity.status(status).contentType(MediaType.TEXT_PLAIN).body(body);
  }

  private String msg(Throwable ex, String fallback) {
    if (ex.getMessage() == null || ex.getMessage().isBlank()) return fallback;

    return ex.getMessage();
  }
}
