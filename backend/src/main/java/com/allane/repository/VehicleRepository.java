package com.allane.repository;

import com.allane.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findTop10ByBrandIgnoreCaseStartingWith(String brand);
}
