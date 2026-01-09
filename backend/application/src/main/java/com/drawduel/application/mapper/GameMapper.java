package com.drawduel.application.mapper;

import com.drawduel.application.dtos.GameDto;
import com.drawduel.domain.models.Game;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameMapper {
  public GameDto toDto(Game entity);

  public Game toDomain(GameDto dto);
}
