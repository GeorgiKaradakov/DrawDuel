package com.drawduel.tests.infrastructure.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.drawduel.application.dtos.RegisterRequestDto;
import com.drawduel.application.ports.RegisterUseCasePort;
import com.drawduel.tests.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
class DashboardControllerIntegrationTest extends BaseIntegrationTest {

  @Autowired MockMvc mockMvc;

  @Autowired RegisterUseCasePort registerUseCase;

  private String registerAndGetAccessToken() {
    RegisterRequestDto req = new RegisterRequestDto();
    req.setEmail("test@example.com");
    req.setUsername("testuser");
    req.setPass("password123");
    req.setRepeatPass("password123");
    req.setIpAddress("127.0.0.1");
    req.setUserAgent("JUnit");
    req.setLocation("test");

    var result = registerUseCase.handle(new RegisterUseCasePort.Query(req));

    return result.response().getAccessToken();
  }

  @Test
  void dashboardEndpointReturns200_forRegisteredUser() throws Exception {
    String accessToken = registerAndGetAccessToken();

    mockMvc
        .perform(get("/api/dashboard/get").header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isOk());
  }
}
