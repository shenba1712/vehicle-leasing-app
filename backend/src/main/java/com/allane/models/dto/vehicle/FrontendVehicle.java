package com.allane.models.dto.vehicle;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Year;

@Getter
@Setter
@Builder
public class FrontendVehicle {
    Long id;
    String brand;
    String model;
    Year modelYear;
    String vin;
    Double price;
}
