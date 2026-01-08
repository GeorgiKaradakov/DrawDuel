package com.drawduel.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class GamesPerDayDto {

  public String day;
  public long games;
}
