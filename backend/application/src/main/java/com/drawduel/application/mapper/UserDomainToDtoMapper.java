package com.drawduel.application.mapper;

import com.drawduel.application.dtos.UserDto;
import com.drawduel.domain.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDomainToDtoMapper {
  // Domain to Dto
  @Mapping(target = "createdAt", source = "createdAt")
  UserDto toUserDto(User user);
}
