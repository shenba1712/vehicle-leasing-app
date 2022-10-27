package com.allane.service;

import com.allane.models.dto.vehicle.NewVehicle;
import com.allane.models.dto.vehicle.FrontendVehicle;
import com.allane.models.dto.vehicle.FrontendVehicles;

public interface VehicleService {
    FrontendVehicle getVehicleDetails(long id);

    void addVehicle(NewVehicle newVehicle);

    void updateVehicle(long id, NewVehicle updatedVehicleDetails);

    FrontendVehicles getVehicles(int pageNumber);
}
