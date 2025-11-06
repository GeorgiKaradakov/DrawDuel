package com.drawduel.infrastructure.api;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/dashboard")
public class DashboardController {
  @GetMapping("/get")
  public ResponseEntity<String> getDashboard() {
    return ResponseEntity.ok("Welcome to the Dashboard!");
  }
}
