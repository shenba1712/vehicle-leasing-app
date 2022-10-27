package com.allane.models.dto.contract;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FrontendLeasingVehicle {
    long id;
    String vehicleModel;
    String vin;
    double price;
}
