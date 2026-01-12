package com.drawduel.application.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileRequestDto {
  private String username;
  private String email;
}
