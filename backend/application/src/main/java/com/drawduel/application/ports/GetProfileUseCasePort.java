package com.drawduel.application.ports;

import com.drawduel.application.dtos.UserProfileDto;
import java.util.UUID;

public interface GetProfileUseCasePort {

  record Query(UUID userId) {}

  record Result(UserProfileDto profile) {}

  Result handle(Query query);
}
