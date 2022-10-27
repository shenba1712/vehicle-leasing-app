package com.allane.service.impl;

import com.allane.models.dto.vehicle.NewVehicle;
import com.allane.models.Vehicle;
import com.allane.models.dto.vehicle.FrontendVehicle;
import com.allane.models.dto.vehicle.FrontendVehicles;
import com.allane.repository.VehicleRepository;
import com.allane.service.mapping.VehicleMappingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Year;
import java.util.List;
import java.util.Optional;

import static com.allane.TestUtil.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
class VehicleServiceImplTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleMappingService mappingService;

    @InjectMocks
    private VehicleServiceImpl service;

    @Test
    public void getAllVehiclesTest() {
        // Prepare
        Vehicle vehicle1 = createVehicle(1, 3400, false);
        Vehicle vehicle2 = createVehicle(2, 25000, true);
        Vehicle vehicle3 = createVehicle(3, 16800, true);

        Page<Vehicle> page = new PageImpl<>(List.of(vehicle1, vehicle2, vehicle3));
        when(vehicleRepository.findAll(PageRequest.of(1, 50))).thenReturn(page);
        when(mappingService.mapToFrontendVehicle(any(Vehicle.class))).thenCallRealMethod();

        FrontendVehicle frontendVehicle1 = createFrontendVehicle(vehicle1);
        FrontendVehicle frontendVehicle2 = createFrontendVehicle(vehicle2);
        FrontendVehicle frontendVehicle3 = createFrontendVehicle(vehicle3);

        FrontendVehicles frontendVehicles = new FrontendVehicles();
        frontendVehicles.setTotalResults(3);
        frontendVehicles.setVehicles(List.of(frontendVehicle1, frontendVehicle2, frontendVehicle3));

        //Act
        FrontendVehicles actualVehicles = service.getVehicles(1);

        // Assert
        verify(vehicleRepository, times(1)).findAll(any(Pageable.class));
        verify(mappingService, times(3)).mapToFrontendVehicle(any(Vehicle.class));
        assertEquals(3, actualVehicles.getVehicles().size());
        assertThat(frontendVehicle1).usingRecursiveComparison().isEqualTo(actualVehicles.getVehicles().get(0));
        assertThat(frontendVehicle2).usingRecursiveComparison().isEqualTo(actualVehicles.getVehicles().get(1));
        assertThat(frontendVehicle3).usingRecursiveComparison().isEqualTo(actualVehicles.getVehicles().get(2));
    }

    @Nested
    public class GetVehicleDetails {

        @Test
        public void notFound() {
            when(vehicleRepository.findById(anyLong())).thenReturn(Optional.empty());

            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                service.getVehicleDetails(1);
            });

            verify(mappingService, never()).mapToFrontendVehicle(any(Vehicle.class));
        }

        @Test
        public void success() {
            Vehicle vehicle = createVehicle(1, 10000, true);
            when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
            when(mappingService.mapToFrontendVehicle(vehicle)).thenCallRealMethod();

            FrontendVehicle frontendVehicle = createFrontendVehicle(vehicle);

            FrontendVehicle actualVehicle = service.getVehicleDetails(1L);

            verify(vehicleRepository).findById(1L);
            verify(mappingService).mapToFrontendVehicle(vehicle);

            assertThat(actualVehicle).usingRecursiveComparison().isEqualTo(frontendVehicle);
        }
    }

    @Nested
    public class AddVehicle {
        NewVehicle newVehicle;

        @BeforeEach
        public void setUp() {
            newVehicle = new NewVehicle();
            newVehicle.setBrand("Mercedes");
            newVehicle.setPrice(23000.00);
            newVehicle.setModel("CLA");
            newVehicle.setModelYear(Year.of(2019));
            newVehicle.setVin(null);
        }

        @Test
        public void success() {
            service.addVehicle(newVehicle);

            ArgumentCaptor<Vehicle> captor = ArgumentCaptor.forClass(Vehicle.class);
            verify(vehicleRepository).save(captor.capture());
            Vehicle vehicle = captor.getValue();
            assertThat(vehicle.getVin()).isEqualTo(newVehicle.getVin());
            assertThat(vehicle.getBrand()).isEqualTo(newVehicle.getBrand());
            assertThat(vehicle.getPrice()).isEqualTo(newVehicle.getPrice());
            assertThat(vehicle.getModel()).isEqualTo(newVehicle.getModel());
            assertThat(vehicle.getModelYear()).isEqualTo(newVehicle.getModelYear());
        }
    }

    @Nested
    public class UpdateVehicle {
        NewVehicle updateVehicle;

        @BeforeEach
        public void setUp() {
            updateVehicle = new NewVehicle();
            updateVehicle.setBrand("Mercedes");
            updateVehicle.setPrice(23000.00);
            updateVehicle.setModel("CLA");
            updateVehicle.setModelYear(Year.of(2019));
            updateVehicle.setVin(null);

            Vehicle vehicle = createVehicle(1, 10000, true);
            when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        }

        @Test
        public void vehicleNotFound() {
            when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());

            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                service.updateVehicle(1L, updateVehicle);
            });

            verify(vehicleRepository, never()).save(any(Vehicle.class));
        }

        @Test
        public void success() {
            service.updateVehicle(1L, updateVehicle);

            ArgumentCaptor<Vehicle> captor = ArgumentCaptor.forClass(Vehicle.class);
            verify(vehicleRepository).save(captor.capture());
            Vehicle vehicle = captor.getValue();
            assertThat(vehicle.getId()).isEqualTo(1L);
            assertThat(vehicle.getVin()).isEqualTo(updateVehicle.getVin());
            assertThat(vehicle.getBrand()).isEqualTo(updateVehicle.getBrand());
            assertThat(vehicle.getPrice()).isEqualTo(updateVehicle.getPrice());
            assertThat(vehicle.getModel()).isEqualTo(updateVehicle.getModel());
            assertThat(vehicle.getModelYear()).isEqualTo(updateVehicle.getModelYear());
        }
    }

}
