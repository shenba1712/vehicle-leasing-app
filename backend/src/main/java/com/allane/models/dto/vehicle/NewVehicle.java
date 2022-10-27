package com.allane.models.dto.vehicle;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Year;

@Getter
@Setter
public class NewVehicle {
    @NotBlank
    private String brand;
    @NotBlank
    private String model;
    @NotNull
    private Year modelYear;
    @Nullable
    private String vin;
    @NotNull
    private Double price;
}
