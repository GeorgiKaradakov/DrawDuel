package com.drawduel.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterRequestDto {
  private String email;
  private String username;
  private String pass;
  private String repeatPass;

  private String ipAddress;
  private String userAgent;
  private String location;
}
