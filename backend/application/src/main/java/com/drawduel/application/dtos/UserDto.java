package com.drawduel.application.dtos;

import java.time.Instant;

public record UserDto(String id, String username, String email, Instant createdAt) {}
