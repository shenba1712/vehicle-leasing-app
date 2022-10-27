package com.allane.controller;

import com.allane.models.dto.vehicle.NewVehicle;
import com.allane.models.Vehicle;
import com.allane.models.dto.vehicle.FrontendVehicle;
import com.allane.models.dto.vehicle.FrontendVehicles;
import com.allane.service.VehicleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Year;
import java.util.List;

import static com.allane.TestUtil.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(VehicleController.class)
class VehicleControllerTest {

    @MockBean
    private VehicleService vehicleService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getAllVehicles() throws Exception {
        Vehicle vehicle1 = createVehicle(1, 12300, false);
        FrontendVehicle frontendVehicle1 = createFrontendVehicle(vehicle1);

        Vehicle vehicle2 = createVehicle(2, 45000, true);
        FrontendVehicle frontendVehicle2 = createFrontendVehicle(vehicle2);

        FrontendVehicles frontendVehicles = new FrontendVehicles();
        frontendVehicles.setTotalResults(15L);
        frontendVehicles.setVehicles(List.of(frontendVehicle1, frontendVehicle2));

        when(vehicleService.getVehicles(anyInt())).thenReturn(frontendVehicles);

        mockMvc.perform(get("/vehicle/page/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalResults", Matchers.is(15)))
                .andExpect(jsonPath("$.vehicles", Matchers.hasSize(2)))

                .andExpect(jsonPath("$.vehicles[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$.vehicles[0].brand", Matchers.is(frontendVehicle1.getBrand())))
                .andExpect(jsonPath("$.vehicles[0].model", Matchers.is(frontendVehicle1.getModel())))
                .andExpect(jsonPath("$.vehicles[0].modelYear", Matchers.is(frontendVehicle1.getModelYear().toString())))
                .andExpect(jsonPath("$.vehicles[0].price", Matchers.is(frontendVehicle1.getPrice())))
                .andExpect(jsonPath("$.vehicles[0].vin", Matchers.is(frontendVehicle1.getVin())))

                .andExpect(jsonPath("$.vehicles[1].id", Matchers.is(2)))
                .andExpect(jsonPath("$.vehicles[1].brand", Matchers.is(frontendVehicle2.getBrand())))
                .andExpect(jsonPath("$.vehicles[1].model", Matchers.is(frontendVehicle2.getModel())))
                .andExpect(jsonPath("$.vehicles[1].modelYear", Matchers.is(frontendVehicle2.getModelYear().toString())))
                .andExpect(jsonPath("$.vehicles[1].price", Matchers.is(frontendVehicle2.getPrice())))
                .andExpect(jsonPath("$.vehicles[1].vin", Matchers.is(frontendVehicle2.getVin())));
    }

    @Nested
    public class GetVehicle {
        @Test
        public void notFound() throws Exception {
            when(vehicleService.getVehicleDetails(anyLong())).thenThrow(new IllegalArgumentException());

            mockMvc.perform(get("/vehicle/1222"))
                    .andExpect(status().isNotFound());
        }

        @Test
        public void randomError() throws Exception {
            when(vehicleService.getVehicleDetails(anyLong())).thenThrow(new RuntimeException("Random error"));

            mockMvc.perform(get("/vehicle/1222"))
                    .andExpect(status().isInternalServerError());
        }

        @Test
        public void success() throws Exception {
            Vehicle vehicle = createVehicle(1, 12300, false);
            FrontendVehicle frontendVehicle = createFrontendVehicle(vehicle);
            when(vehicleService.getVehicleDetails(1L)).thenReturn(frontendVehicle);

            mockMvc.perform(get("/vehicle/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", Matchers.is(1)))
                    .andExpect(jsonPath("$.brand", Matchers.is("BMW")))
                    .andExpect(jsonPath("$.model", Matchers.is("model1")))
                    .andExpect(jsonPath("$.modelYear", Matchers.is("1992")))
                    .andExpect(jsonPath("$.vin", Matchers.nullValue()))
                    .andExpect(jsonPath("$.price", Matchers.is(12300.0)));
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
        public void randomError() throws Exception {
            doThrow(new RuntimeException("Random error")).when(vehicleService).addVehicle(any(NewVehicle.class));

            mockMvc.perform(post("/vehicle")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(newVehicle)))
                    .andExpect(status().isInternalServerError());
        }

        @Test
        public void success() throws Exception {
            mockMvc.perform(post("/vehicle")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(newVehicle)))
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    public class UpdateVehicleDetails {
        NewVehicle updateVehicle;

        @BeforeEach
        public void setUp() {
            updateVehicle = new NewVehicle();
            updateVehicle.setBrand("Mercedes");
            updateVehicle.setPrice(23000.00);
            updateVehicle.setModel("CLA");
            updateVehicle.setModelYear(Year.of(2019));
            updateVehicle.setVin(null);
        }

        @Test
        public void illegalArguments() throws Exception {
            doThrow(new IllegalArgumentException()).when(vehicleService).updateVehicle(anyLong(), any(NewVehicle.class));

            mockMvc.perform(put("/vehicle/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateVehicle)))
                    .andExpect(status().isUnprocessableEntity());
        }

        @Test
        public void randomError() throws Exception {
            doThrow(new RuntimeException("Random error")).when(vehicleService).updateVehicle(anyLong(), any(NewVehicle.class));

            mockMvc.perform(put("/vehicle/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateVehicle)))
                    .andExpect(status().isInternalServerError());
        }

        @Test
        public void success() throws Exception {
            mockMvc.perform(put("/vehicle/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateVehicle)))
                    .andExpect(status().isNoContent());
        }
    }
}
