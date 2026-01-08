package com.drawduel.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GamesPerDay {
  public String day;
  public int games;
}
