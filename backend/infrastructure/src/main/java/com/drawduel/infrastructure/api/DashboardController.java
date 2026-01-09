package com.drawduel.infrastructure.api;

import com.drawduel.application.dtos.DashboardResponseDto;
import com.drawduel.application.ports.GetDashboardUseCasePort;
import com.drawduel.application.services.JwtService;
import java.util.UUID;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
// @RequiredArgsConstructor
public class DashboardController {

  private final GetDashboardUseCasePort getDashboardUseCase;
  private final JwtService jwtService;

  public DashboardController(JwtService jwtService, GetDashboardUseCasePort getDashboardUseCase) {
    this.jwtService = jwtService;
    this.getDashboardUseCase = getDashboardUseCase;
  }

  @GetMapping("/get")
  public DashboardResponseDto getDashboard(@RequestHeader("Authorization") String auth) {
    UUID userId = jwtService.extractUserId(auth.substring(7));
    DashboardResponseDto responseDto =
        getDashboardUseCase.handle(new GetDashboardUseCasePort.Query(userId)).dashboardResponse();

    return responseDto;
  }
}
