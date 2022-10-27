package com.allane.controller;

import com.allane.models.Customer;
import com.allane.models.LeasingContract;
import com.allane.models.dto.contract.NewContract;
import com.allane.models.Vehicle;
import com.allane.models.dto.contract.FrontendLeasingContract;
import com.allane.models.dto.contract.FrontendLeasingContracts;
import com.allane.models.dto.contract.FrontendLeasingCustomer;
import com.allane.models.dto.contract.FrontendLeasingVehicle;
import com.allane.service.ContractService;
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

import java.util.List;

import static com.allane.TestUtil.*;
import static com.allane.TestUtil.createContract;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ContractController.class)
class ContractControllerTest {

    @MockBean
    private ContractService contractService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getAllContracts() throws Exception {
        Customer customer1 = createCustomer(1);
        Vehicle vehicle1 = createVehicle(1, 12300, false);
        LeasingContract contract1 = createContract(1, customer1, vehicle1, 150);
        Vehicle vehicle2 = createVehicle(2, 23300, true);
        LeasingContract contract2 = createContract(2, customer1, vehicle2, 399);

        Customer customer2 = createCustomer(2);
        Vehicle vehicle3 = createVehicle(3, 15000, false);
        LeasingContract contract3 = createContract(3, customer2, vehicle3, 275);

        FrontendLeasingContract frontendLeasingContract1 = createFrontendLeasingContract(contract1);
        FrontendLeasingContract frontendLeasingContract2 = createFrontendLeasingContract(contract2);
        FrontendLeasingContract frontendLeasingContract3 = createFrontendLeasingContract(contract3);

        FrontendLeasingContracts contracts = new FrontendLeasingContracts();
        contracts.setTotalResults(3L);
        contracts.setContracts(List.of(frontendLeasingContract1, frontendLeasingContract2, frontendLeasingContract3));

        when(contractService.getAllContracts(anyInt())).thenReturn(contracts);

        mockMvc.perform(get("/contract/overview/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalResults", Matchers.is(3)))
                .andExpect(jsonPath("$.contracts", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.contracts[0].contractNumber", Matchers.is(1)))
                .andExpect(jsonPath("$.contracts[0].monthlyRate", Matchers.is(150.0)))
                .andExpect(jsonPath("$.contracts[0].customer.id", Matchers.is(1)))
                .andExpect(jsonPath("$.contracts[0].customer.fullName", Matchers.is("Max1 Mustermann1")))
                .andExpect(jsonPath("$.contracts[0].vehicle.vehicleModel", Matchers.is("BMW model1 (1992)")))
                .andExpect(jsonPath("$.contracts[0].vehicle.vin", Matchers.nullValue()))
                .andExpect(jsonPath("$.contracts[0].vehicle.price", Matchers.is(12300.0)))

                .andExpect(jsonPath("$.contracts[1].contractNumber", Matchers.is(2)))
                .andExpect(jsonPath("$.contracts[1].customer.id", Matchers.is(1)))
                .andExpect(jsonPath("$.contracts[1].customer.fullName", Matchers.is("Max1 Mustermann1")))
                .andExpect(jsonPath("$.contracts[1].monthlyRate", Matchers.is(399.0)))
                .andExpect(jsonPath("$.contracts[1].vehicle.vehicleModel", Matchers.is("BMW model2 (1992)")))
                .andExpect(jsonPath("$.contracts[1].vehicle.vin", Matchers.is("vin2")))
                .andExpect(jsonPath("$.contracts[1].vehicle.price", Matchers.is(23300.0)))

                .andExpect(jsonPath("$.contracts[2].contractNumber", Matchers.is(3)))
                .andExpect(jsonPath("$.contracts[2].customer.id", Matchers.is(2)))
                .andExpect(jsonPath("$.contracts[2].customer.fullName", Matchers.is("Max2 Mustermann2")))
                .andExpect(jsonPath("$.contracts[2].monthlyRate", Matchers.is(275.0)))
                .andExpect(jsonPath("$.contracts[2].vehicle.vehicleModel", Matchers.is("BMW model3 (1992)")))
                .andExpect(jsonPath("$.contracts[2].vehicle.vin", Matchers.nullValue()))
                .andExpect(jsonPath("$.contracts[2].vehicle.price", Matchers.is(15000.0)));
    }

    @Nested
    public class GetContract {
        @Test
        public void wrongContractNumber() throws Exception {
            when(contractService.getContract(anyLong())).thenThrow(new IllegalArgumentException());

            mockMvc.perform(get("/contract/1222"))
                    .andExpect(status().isNotFound());
        }

        @Test
        public void randomError() throws Exception {
            when(contractService.getContract(anyLong())).thenThrow(new RuntimeException("Random error"));

            mockMvc.perform(get("/contract/1222"))
                    .andExpect(status().isInternalServerError());
        }

        @Test
        public void success() throws Exception {
            Customer customer = createCustomer(1);
            Vehicle vehicle = createVehicle(1, 12300, false);
            LeasingContract contract = createContract(1, customer, vehicle, 150);
            FrontendLeasingContract frontendLeasingContract = createFrontendLeasingContract(contract);
            when(contractService.getContract(1L)).thenReturn(frontendLeasingContract);

            mockMvc.perform(get("/contract/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.contractNumber", Matchers.is(1)))
                    .andExpect(jsonPath("$.monthlyRate", Matchers.is(150.0)))
                    .andExpect(jsonPath("$.customer.id", Matchers.is(1)))
                    .andExpect(jsonPath("$.customer.fullName", Matchers.is("Max1 Mustermann1")))
                    .andExpect(jsonPath("$.vehicle.vehicleModel", Matchers.is("BMW model1 (1992)")))
                    .andExpect(jsonPath("$.vehicle.vin", Matchers.nullValue()))
                    .andExpect(jsonPath("$.vehicle.price", Matchers.is(12300.0)));
        }
    }

    @Nested
    public class AddContract {
        NewContract newContract;

        @BeforeEach
        public void setUp() {
            newContract = new NewContract();
            newContract.setVehicleId(1L);
            newContract.setCustomerId(1L);
            newContract.setMonthlyRate(250.99);
        }
        @Test
        public void illegalArguments() throws Exception {
            doThrow(new IllegalArgumentException()).when(contractService).registerContract(any(NewContract.class));

            mockMvc.perform(post("/contract")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(newContract)))
                    .andExpect(status().isUnprocessableEntity());
        }

        @Test
        public void randomError() throws Exception {
            doThrow(new RuntimeException("Random error")).when(contractService).registerContract(any(NewContract.class));

            mockMvc.perform(post("/contract")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(newContract)))
                    .andExpect(status().isInternalServerError());
        }

        @Test
        public void success() throws Exception {
            mockMvc.perform(post("/contract")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(newContract)))
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    public class EditContract {
        NewContract updatedContract;

        @BeforeEach
        public void setUp() {
            updatedContract = new NewContract();
            updatedContract.setVehicleId(1L);
            updatedContract.setCustomerId(1L);
            updatedContract.setMonthlyRate(250.99);
        }
        @Test
        public void illegalArguments() throws Exception {
            doThrow(new IllegalArgumentException()).when(contractService).updateContract(anyLong(), any(NewContract.class));

            mockMvc.perform(put("/contract/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updatedContract)))
                    .andExpect(status().isUnprocessableEntity());
        }

        @Test
        public void randomError() throws Exception {
            doThrow(new RuntimeException("Random error")).when(contractService).updateContract(anyLong(), any(NewContract.class));

            mockMvc.perform(put("/contract/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updatedContract)))
                    .andExpect(status().isInternalServerError());
        }

        @Test
        public void success() throws Exception {
            mockMvc.perform(put("/contract/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updatedContract)))
                    .andExpect(status().isNoContent());
        }
    }

    @Test
    public void getFilteredVehicles() throws Exception {
        Vehicle vehicle1 = createVehicle(1, 12300, false);
        FrontendLeasingVehicle frontendLeasingVehicle1 = createFrontendLeasingVehicle(vehicle1);

        Vehicle vehicle2 = createVehicle(2, 34000, false);
        FrontendLeasingVehicle frontendLeasingVehicle2 = createFrontendLeasingVehicle(vehicle2);

        when(contractService.filterVehicles(anyString())).thenReturn(List.of(frontendLeasingVehicle1, frontendLeasingVehicle2));

        mockMvc.perform(get("/contract/filter/vehicles?brandQuery=BMW"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)));
    }

    @Test
    public void getFilteredCustomers() throws Exception {
        Customer customer1 = createCustomer(1);
        FrontendLeasingCustomer frontendLeasingCustomer1 = createFrontendLeasingCustomer(customer1);

        Customer customer2 = createCustomer(2);
        FrontendLeasingCustomer frontendLeasingCustomer2 = createFrontendLeasingCustomer(customer2);

        Customer customer3 = createCustomer(3);
        FrontendLeasingCustomer frontendLeasingCustomer3 = createFrontendLeasingCustomer(customer3);

        when(contractService.filterCustomers(anyString())).thenReturn(List.of(frontendLeasingCustomer1, frontendLeasingCustomer3));

        mockMvc.perform(get("/contract/filter/customers?query=Muster"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)));
    }

}
