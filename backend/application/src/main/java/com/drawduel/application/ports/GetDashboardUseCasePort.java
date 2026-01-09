package com.drawduel.application.ports;

import com.drawduel.application.dtos.DashboardResponseDto;
import java.util.UUID;

public interface GetDashboardUseCasePort {
  public record Query(UUID userId) {}

  public record Result(DashboardResponseDto dashboardResponse) {}

  public Result handle(Query q);
}
