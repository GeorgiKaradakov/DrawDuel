package com.drawduel.application.dtos;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
  private UUID id;
  private String username;
  private String email;
  private Instant createdAt;
}
