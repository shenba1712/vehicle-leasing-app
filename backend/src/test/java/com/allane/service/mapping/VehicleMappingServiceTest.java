package com.allane.service.mapping;

import com.allane.models.Vehicle;
import com.allane.models.dto.vehicle.FrontendVehicle;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import static com.allane.TestUtil.createFrontendVehicle;
import static com.allane.TestUtil.createVehicle;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class VehicleMappingServiceTest {

    @InjectMocks
    private VehicleMappingService mappingService;

    @Test
    void mapToFrontendVehicle() {
        Vehicle vehicle = createVehicle(1, 10000, true);
        FrontendVehicle frontendVehicle = createFrontendVehicle(vehicle);

        FrontendVehicle actualVehicle = mappingService.mapToFrontendVehicle(vehicle);

        assertThat(actualVehicle).usingRecursiveComparison().isEqualTo(frontendVehicle);
    }
}
