package com.drawduel.application.usecases;

import com.drawduel.application.ports.GetLocationUseCasePort;

public class GetLocationUseCase implements GetLocationUseCasePort {

  @Override
  public Result handle(Query q) {
    // TODO: Unimplemented method
    return new Result("unknown", "unknown");
  }
}
