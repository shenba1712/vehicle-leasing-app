package com.allane.controller;

import com.allane.models.dto.vehicle.NewVehicle;
import com.allane.models.dto.vehicle.FrontendVehicle;
import com.allane.models.dto.vehicle.FrontendVehicles;
import com.allane.service.VehicleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
@Slf4j
@RequestMapping("vehicle")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/page/{pageNumber}")
    public FrontendVehicles getAllVehicles(@PathVariable int pageNumber) {
        return vehicleService.getVehicles(pageNumber);
    }

    @GetMapping("{id}")
    public ResponseEntity<FrontendVehicle> getVehicle(@PathVariable long id) {
        try {
            return ResponseEntity.ok(vehicleService.getVehicleDetails(id));
        } catch (IllegalArgumentException e) {
            log.error("Unable to find vehicle with id: " + id, e);
            return new ResponseEntity<>(NOT_FOUND);
        } catch (Exception e) {
            log.error("Unexpected error. Cannot find vehicle with id: " + id, e);
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Void> addVehicle(@Valid @RequestBody NewVehicle newVehicle) {
        try {
            vehicleService.addVehicle(newVehicle);
            return new ResponseEntity<>(NO_CONTENT);
        } catch (Exception e) {
            log.error("Unexpected error. Cannot add new vehicle", e);
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> editVehicle(@PathVariable long id, @Valid @RequestBody NewVehicle updatedVehicle) {
        try {
            vehicleService.updateVehicle(id, updatedVehicle);
            return new ResponseEntity<>(NO_CONTENT);
        } catch (IllegalArgumentException e) {
            log.error("Cannot update vehicle", e);
            return new ResponseEntity<>(UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            log.error("Unexpected error. Cannot update vehicle", e);
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
    }
}
