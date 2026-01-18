package com.drawduel.application.ports;

public interface ChangePasswordUseCasePort {

  public record Query(java.util.UUID userId, String currentPassword, String newPassword) {}

  public void handle(Query query);
}
