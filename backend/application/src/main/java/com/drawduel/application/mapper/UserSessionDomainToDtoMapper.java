package com.drawduel.application.mapper;

import com.drawduel.application.dtos.UserSessionDto;
import com.drawduel.domain.models.UserSession;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserSessionDomainToDtoMapper {
  // Domain to Dto
  UserSessionDto toDto(UserSession userSession);

  // Dto to Domain
  UserSession toDomain(UserSessionDto userSessionDto);
}
