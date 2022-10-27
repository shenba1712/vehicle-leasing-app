package com.allane.service.mapping;

import com.allane.models.Vehicle;
import com.allane.models.dto.vehicle.FrontendVehicle;
import org.springframework.stereotype.Service;

@Service
public class VehicleMappingService {

    public FrontendVehicle mapToFrontendVehicle(Vehicle vehicle) {
        return FrontendVehicle.builder()
                .id(vehicle.getId())
                .brand(vehicle.getBrand())
                .model(vehicle.getModel())
                .modelYear(vehicle.getModelYear())
                .vin(vehicle.getVin())
                .price(vehicle.getPrice())
                .build();
    }
}
