package com.allane.service.impl;

import com.allane.models.Customer;
import com.allane.models.LeasingContract;
import com.allane.models.dto.contract.NewContract;
import com.allane.models.Vehicle;
import com.allane.models.dto.contract.FrontendLeasingContract;
import com.allane.models.dto.contract.FrontendLeasingContracts;
import com.allane.models.dto.contract.FrontendLeasingCustomer;
import com.allane.models.dto.contract.FrontendLeasingVehicle;
import com.allane.repository.CustomerRepository;
import com.allane.repository.ContractRepository;
import com.allane.repository.VehicleRepository;
import com.allane.service.mapping.ContractMappingService;
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

import java.util.List;
import java.util.Optional;

import static com.allane.TestUtil.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ContractServiceImplTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private ContractMappingService mappingService;

    @InjectMocks
    private ContractServiceImpl service;

    @Test
    public void getAllContractsTest() {
        // Prepare
        Customer customer1 = createCustomer(1);
        Vehicle vehicle1 = createVehicle(1, 12300, false);
        LeasingContract contract1 = createContract(1, customer1, vehicle1, 150);
        Vehicle vehicle2 = createVehicle(2, 23300, true);
        LeasingContract contract2 = createContract(2, customer1, vehicle2, 399);

        Customer customer2 = createCustomer(2);
        Vehicle vehicle3 = createVehicle(3, 15000, false);
        LeasingContract contract3 = createContract(3, customer2, vehicle3, 275);

        Page<LeasingContract> page = new PageImpl<>(List.of(contract1, contract2, contract3));
        when(contractRepository.findAll(PageRequest.of(1, 50))).thenReturn(page);
        when(mappingService.mapToFrontendLeasingContract(any(LeasingContract.class))).thenCallRealMethod();

        FrontendLeasingContract frontendLeasingContract1 = createFrontendLeasingContract(contract1);
        FrontendLeasingContract frontendLeasingContract2 = createFrontendLeasingContract(contract2);
        FrontendLeasingContract frontendLeasingContract3 = createFrontendLeasingContract(contract3);

        FrontendLeasingContracts contracts = new FrontendLeasingContracts();
        contracts.setContracts(List.of(frontendLeasingContract1, frontendLeasingContract2, frontendLeasingContract3));
        contracts.setTotalResults(3);

        //Act
        FrontendLeasingContracts actualContracts = service.getAllContracts(1);

        // Assert
        verify(contractRepository, times(1)).findAll(any(Pageable.class));
        verify(mappingService, times(3)).mapToFrontendLeasingContract(any(LeasingContract.class));
        assertEquals(3, actualContracts.getContracts().size());
    }

    @Nested
    public class GetContract {

        @Test
        public void notFound() {
            when(contractRepository.findById(anyLong())).thenReturn(Optional.empty());

            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                service.getContract(1);
            });

            verify(mappingService, never()).mapToFrontendLeasingContract(any(LeasingContract.class));
        }

        @Test
        public void success() {
            Customer customer = createCustomer(1);
            Vehicle vehicle = createVehicle(1, 10000, true);
            LeasingContract contract = createContract(1, customer, vehicle, 450);
            when(contractRepository.findByContractNumber(1L)).thenReturn(Optional.of(contract));
            when(mappingService.mapToFrontendLeasingContract(contract)).thenCallRealMethod();

            service.getContract(1L);

            verify(contractRepository).findByContractNumber(1L);
            verify(mappingService).mapToFrontendLeasingContract(contract);
        }
    }

    @Nested
    public class RegisterContract {
        NewContract newContract;

        @BeforeEach
        public void setUp() {
            newContract = new NewContract();
            newContract.setVehicleId(1L);
            newContract.setCustomerId(1L);
            newContract.setMonthlyRate(250.99);
        }

        @Test
        public void customerNotFound() {
            when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                service.registerContract(newContract);
            });

            verify(vehicleRepository, never()).findById(1L);
            verify(contractRepository, never()).save(any(LeasingContract.class));
        }

        @Test
        public void vehicleNotFound() {
            Customer customer = createCustomer(1);
            when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
            when(vehicleRepository.findById(anyLong())).thenReturn(Optional.empty());

            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                service.registerContract(newContract);
            });

            verify(customerRepository).findById(1L);
            verify(vehicleRepository).findById(1L);
            verify(contractRepository, never()).save(any(LeasingContract.class));
        }

        @Test
        public void vehicleAlreadyBooked() {
            Customer customer = createCustomer(1);
            when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
            Vehicle vehicle = createVehicle(1, 10000, true);
            LeasingContract leasingContract = createContract(1, customer, vehicle, 333.0);
            vehicle.setLeasingContract(leasingContract);

            when(vehicleRepository.findById(anyLong())).thenReturn(Optional.of(vehicle));

            Assertions.assertThrows(IllegalStateException.class, () -> {
                service.registerContract(newContract);
            });

            verify(customerRepository).findById(1L);
            verify(vehicleRepository).findById(1L);
            verify(contractRepository, never()).save(any(LeasingContract.class));
        }

        @Test
        public void success() {
            Customer customer = createCustomer(1);
            when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
            Vehicle vehicle = createVehicle(1, 10000, true);
            when(vehicleRepository.findById(anyLong())).thenReturn(Optional.of(vehicle));

            service.registerContract(newContract);

            ArgumentCaptor<LeasingContract> captor = ArgumentCaptor.forClass(LeasingContract.class);
            verify(contractRepository).save(captor.capture());
            LeasingContract contract = captor.getValue();
            assertThat(contract.getMonthlyRate()).isEqualTo(newContract.getMonthlyRate());
            assertThat(contract.getVehicle()).usingRecursiveComparison().isEqualTo(vehicle);
            assertThat(contract.getCustomer()).usingRecursiveComparison().isEqualTo(customer);
        }
    }

    @Nested
    public class UpdateContract {
        NewContract updatedContract;

        @BeforeEach
        public void setUp() {
            updatedContract = new NewContract();
            updatedContract.setVehicleId(2L);
            updatedContract.setCustomerId(2L);
            updatedContract.setMonthlyRate(350.99);

            Customer customer = createCustomer(1);
            when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
            Vehicle vehicle = createVehicle(1, 10000, true);
            when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
            LeasingContract leasingContract = createContract(1, customer, vehicle, 250);
            when(contractRepository.findByContractNumber(1L)).thenReturn(Optional.of(leasingContract));
        }

        @Test
        public void customerNotFound() {
            when(customerRepository.findById(2L)).thenReturn(Optional.empty());

            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                service.updateContract(1L, updatedContract);
            });

            verify(vehicleRepository, never()).findById(2L);
            verify(contractRepository, never()).save(any(LeasingContract.class));
        }

        @Test
        public void vehicleNotFound() {
            Customer customer = createCustomer(2);
            when(customerRepository.findById(2L)).thenReturn(Optional.of(customer));
            when(vehicleRepository.findById(2L)).thenReturn(Optional.empty());

            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                service.updateContract(1L, updatedContract);
            });

            verify(customerRepository).findById(2L);
            verify(vehicleRepository).findById(2L);
            verify(contractRepository, never()).save(any(LeasingContract.class));
        }

        @Test
        public void success() {
            Customer customer = createCustomer(2);
            when(customerRepository.findById(2L)).thenReturn(Optional.of(customer));
            Vehicle vehicle = createVehicle(2, 10000, true);
            when(vehicleRepository.findById(2L)).thenReturn(Optional.of(vehicle));

            service.updateContract(1L, updatedContract);

            ArgumentCaptor<LeasingContract> captor = ArgumentCaptor.forClass(LeasingContract.class);
            verify(contractRepository).save(captor.capture());
            LeasingContract contract = captor.getValue();
            assertThat(contract.getContractNumber()).isEqualTo(1L);
            assertThat(contract.getMonthlyRate()).isEqualTo(updatedContract.getMonthlyRate());
            assertThat(contract.getVehicle()).usingRecursiveComparison().isEqualTo(vehicle);
            assertThat(contract.getCustomer()).usingRecursiveComparison().isEqualTo(customer);
        }
    }

    @Nested
    public class FilterCustomers {

        @Test
        public void getFilteredCustomersWithoutQueryTest() {
            // Prepare
            Customer customer1 = createCustomer(1);
            Customer customer2 = createCustomer(2);

            List<Customer> customers = List.of(customer1, customer2);
            when(customerRepository.findTop10ByLastNameStartingWithIgnoreCase(anyString())).thenReturn(customers);
            when(mappingService.mapToFrontendLeasingCustomer(any(Customer.class))).thenCallRealMethod();

            //Act
            List<FrontendLeasingCustomer> actualCustomers = service.filterCustomers(null);

            // Assert
            verify(customerRepository, times(1)).findTop10ByLastNameStartingWithIgnoreCase("");
            verify(mappingService, times(2)).mapToFrontendLeasingCustomer(any(Customer.class));
            assertEquals(2, actualCustomers.size());
        }

        @Test
        public void getFilteredCustomersWithQueryTest() {
            // Prepare
            Customer customer1 = createCustomer(1);
            Customer customer2 = createCustomer(2);
            Customer customer3 = createCustomer(3);

            List<Customer> customers = List.of(customer1, customer3);
            when(customerRepository.findTop10ByLastNameStartingWithIgnoreCase(anyString())).thenReturn(customers);
            when(mappingService.mapToFrontendLeasingCustomer(any(Customer.class))).thenCallRealMethod();

            //Act
            List<FrontendLeasingCustomer> actualCustomers = service.filterCustomers("Muster");

            // Assert
            verify(customerRepository, times(1)).findTop10ByLastNameStartingWithIgnoreCase("Muster");
            verify(mappingService, times(2)).mapToFrontendLeasingCustomer(any(Customer.class));
            assertEquals(2, actualCustomers.size());
        }

    }

    @Nested
    public class FilterVehicles {

        @Test
        public void getFilteredVehiclesWithoutQueryTest() {
            // Prepare
            Vehicle vehicle1 = createVehicle(1, 45000, false);
            Vehicle vehicle2 = createVehicle(2, 35789, true);

            List<Vehicle> vehicles = List.of(vehicle1, vehicle2);
            when(vehicleRepository.findTop10ByBrandIgnoreCaseStartingWith(anyString()))
                    .thenReturn(vehicles);
            when(mappingService.mapToFrontendVehicle(any(Vehicle.class))).thenCallRealMethod();

            //Act
            List<FrontendLeasingVehicle> actualVehicles = service.filterVehicles(null);

            // Assert
            verify(vehicleRepository, times(1)).findTop10ByBrandIgnoreCaseStartingWith("");
            verify(mappingService, times(2)).mapToFrontendVehicle(any(Vehicle.class));
            assertEquals(2, actualVehicles.size());
        }

        @Test
        public void getFilteredVehiclesWithQueryTest() {
            // Prepare
            Vehicle vehicle1 = createVehicle(1, 45000, false);
            Vehicle vehicle2 = createVehicle(2, 35789, true);
            Vehicle vehicle3 = createVehicle(3, 35789, true);

            List<Vehicle> vehicles = List.of(vehicle1, vehicle2);
            when(vehicleRepository.findTop10ByBrandIgnoreCaseStartingWith(anyString()))
                    .thenReturn(vehicles);
            when(mappingService.mapToFrontendVehicle(any(Vehicle.class))).thenCallRealMethod();

            //Act
            List<FrontendLeasingVehicle> actualVehicles = service.filterVehicles("BMW");

            // Assert
            verify(vehicleRepository, times(1))
                    .findTop10ByBrandIgnoreCaseStartingWith("BMW");
            verify(mappingService, times(2)).mapToFrontendVehicle(any(Vehicle.class));
            assertEquals(2, actualVehicles.size());
        }
    }
}
