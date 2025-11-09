package com.drawduel.infrastructure.mappers;

import com.drawduel.domain.models.UserSession;
import com.drawduel.infrastructure.persistence.jpa.entities.JpaTokensEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserSessionToJpaEntity {
  // Domain to Jpa Entity
  @Mapping(target = "user", ignore = true)
  @Mapping(target = "ipAdress", source = "ipAddress")
  JpaTokensEntity toJpaEntity(UserSession userSession);

  // Jpa Entity to Domain
  @Mapping(target = "userId", source = "user.id")
  @Mapping(target = "ipAddress", source = "ipAdress")
  UserSession toDomain(JpaTokensEntity jpaTokensEntity);
}
