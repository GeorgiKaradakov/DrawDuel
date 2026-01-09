package com.drawduel.application.dtos;

import com.drawduel.domain.enums.GameStatus;
import java.time.Instant;
import java.util.UUID;

public record GameDto(
    UUID id,
    UUID playerAId,
    UUID playerBId,
    int totalRounds,
    int currentRound,
    int playerADrawPoints,
    int playerAGuessPoints,
    int playerBDrawPoints,
    int playerBGuessPoints,
    GameStatus status,
    UUID winnerId,
    Instant startedAt,
    Instant endedAt) {}
