package com.drawduel.application.ports;

public interface GetLocationUseCasePort {
  public record Query(String id) {}

  public record Result(String Country, String city) {}

  public Result handle(Query q);
}
