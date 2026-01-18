package com.drawduel.application.dtos;

import com.drawduel.domain.enums.DeviceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeviceEntryDto {

  private String osName;
  private String browserName;
  private String Location;
  private DeviceStatus Status;
}
