package com.drawduel.application.dtos;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RecentMatchDto {

  public UUID matchId;
  public int drawPoints;
  public int guessPoints;
  public String outcome;
}
