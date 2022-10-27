package com.allane.models.dto.vehicle;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FrontendVehicles {
    long totalResults;
    List<FrontendVehicle> vehicles;
}
