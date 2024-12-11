package com.batch.manager.entity.deviceTracker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDto {
  private  String idText;
  private  String registrationDate;
  private  Double latitude;
  private   Double longitude;
  private  String name;
  private  String code;
  private  String location;
}
