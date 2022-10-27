package com.allane.service.mapping;

import com.allane.models.Customer;
import com.allane.models.LeasingContract;
import com.allane.models.Vehicle;
import com.allane.models.dto.contract.FrontendLeasingContract;
import com.allane.models.dto.contract.FrontendLeasingCustomer;
import com.allane.models.dto.contract.FrontendLeasingVehicle;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class ContractMappingService {

    public FrontendLeasingContract mapToFrontendLeasingContract(LeasingContract contract) {
        return FrontendLeasingContract.builder()
                .contractNumber(contract.getContractNumber())
                .customer(mapToFrontendLeasingCustomer(contract.getCustomer()))
                .monthlyRate(contract.getMonthlyRate())
                .vehicle(mapToFrontendVehicle(contract.getVehicle()))
                .build();
    }

    public FrontendLeasingCustomer mapToFrontendLeasingCustomer(Customer customer) {
        return FrontendLeasingCustomer.builder()
                .id(customer.getId())
                .fullName(customer.getFullName())
                .fullNameWithBirthDate(customer.getFullName()
                        + " (" + customer.getBirthDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + ")")
                .build();
    }

    public FrontendLeasingVehicle mapToFrontendVehicle(Vehicle vehicle) {
        return FrontendLeasingVehicle.builder()
                .id(vehicle.getId())
                .vehicleModel(vehicle.getVehicleModel())
                .vin(vehicle.getVin())
                .price(vehicle.getPrice())
                .build();
    }
}
