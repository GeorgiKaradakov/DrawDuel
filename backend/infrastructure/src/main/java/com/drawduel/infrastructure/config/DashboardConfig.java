package com.drawduel.infrastructure.config;

import com.drawduel.application.ports.GetDashboardUseCasePort;
import com.drawduel.application.usecases.GetDashboardUseCase;
import com.drawduel.domain.ports.DashboardRepository;
import com.drawduel.infrastructure.persistence.jpa.repositories.DashboardJpaRepository;
import com.drawduel.infrastructure.persistence.ports.DashboardRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DashboardConfig {
  @Bean
  public DashboardRepository dashboardRepository(DashboardJpaRepository jpaRepository) {
    return new DashboardRepositoryImpl(jpaRepository);
  }

  @Bean
  public GetDashboardUseCasePort getDashboardUseCase(DashboardRepository repository) {
    return new GetDashboardUseCase(repository);
  }
}
