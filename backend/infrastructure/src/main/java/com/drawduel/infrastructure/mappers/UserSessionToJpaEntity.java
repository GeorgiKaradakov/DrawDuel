package com.drawduel.infrastructure.mappers;

import com.drawduel.domain.models.UserSession;
import com.drawduel.infrastructure.persistence.jpa.entities.JpaTokensEntity;
import com.drawduel.infrastructure.persistence.jpa.entities.JpaUserEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserSessionToJpaEntity {
  // Domain to Jpa Entity
  @Mapping(target = "user", ignore = true)
  @Mapping(target = "ipAdress", source = "ipAddress")
  JpaTokensEntity toJpaEntity(UserSession userSession);

  @AfterMapping
  default void attachUser(UserSession userSession, @MappingTarget JpaTokensEntity entity) {
    JpaUserEntity user = new JpaUserEntity();
    user.setId(userSession.getUserId());
    entity.setUser(user);
  }

  // Jpa Entity to Domain
  @Mapping(target = "userId", source = "user.id")
  @Mapping(target = "ipAddress", source = "ipAdress")
  UserSession toDomain(JpaTokensEntity jpaTokensEntity);
}
