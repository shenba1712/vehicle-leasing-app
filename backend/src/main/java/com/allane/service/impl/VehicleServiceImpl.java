package com.allane.service.impl;

import com.allane.models.dto.vehicle.NewVehicle;
import com.allane.models.Vehicle;
import com.allane.models.dto.vehicle.FrontendVehicle;
import com.allane.models.dto.vehicle.FrontendVehicles;
import com.allane.repository.VehicleRepository;
import com.allane.service.mapping.VehicleMappingService;
import com.allane.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleServiceImpl implements VehicleService {
    static final int MAX_PAGE_SIZE = 50;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private VehicleMappingService mappingService;

    @Override
    public FrontendVehicle getVehicleDetails(long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        return mappingService.mapToFrontendVehicle(vehicle);
    }

    @Override
    public void addVehicle(NewVehicle newVehicle) {
        Vehicle vehicle = new Vehicle();
        vehicle.setVin(newVehicle.getVin());
        vehicle.setBrand(newVehicle.getBrand());
        vehicle.setModel(newVehicle.getModel());
        vehicle.setModelYear(newVehicle.getModelYear());
        vehicle.setPrice(newVehicle.getPrice());
        vehicleRepository.save(vehicle);
    }

    @Override
    public void updateVehicle(long id, NewVehicle updatedVehicleDetails) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find vehicle of id: " + id));
        vehicle.setVin(updatedVehicleDetails.getVin());
        vehicle.setBrand(updatedVehicleDetails.getBrand());
        vehicle.setModel(updatedVehicleDetails.getModel());
        vehicle.setPrice(updatedVehicleDetails.getPrice());
        vehicle.setModelYear(updatedVehicleDetails.getModelYear());
        vehicleRepository.save(vehicle);
    }

    @Override
    public FrontendVehicles getVehicles(int pageNumber) {
        FrontendVehicles vehicles = new FrontendVehicles();
        Page<Vehicle> vehiclePage  = vehicleRepository.findAll(PageRequest.of(pageNumber, MAX_PAGE_SIZE));
        vehicles.setTotalResults(vehiclePage.getTotalElements());
        List<FrontendVehicle> vehicleList = vehiclePage.getContent().stream().map(mappingService::mapToFrontendVehicle).collect(Collectors.toList());
        vehicles.setVehicles(vehicleList);
        return vehicles;
    }
}
