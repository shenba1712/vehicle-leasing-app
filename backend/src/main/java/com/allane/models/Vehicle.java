package com.allane.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.Year;

@Entity(name = "vehicle")
@Getter
@Setter
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;

    private String model;

    @Column(name = "model_year")
    private Year modelYear;

    @Nullable
    private String vin;

    private Double price;

    @OneToOne(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private LeasingContract leasingContract;

    public String getVehicleModel() {
        return getBrand() + " " + getModel() + " (" + getModelYear() + ")";
    }
}
