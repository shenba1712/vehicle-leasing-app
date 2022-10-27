package com.allane;

import com.allane.models.Customer;
import com.allane.models.LeasingContract;
import com.allane.models.Vehicle;
import com.allane.models.dto.customer.FrontendCustomer;
import com.allane.models.dto.contract.FrontendLeasingContract;
import com.allane.models.dto.contract.FrontendLeasingCustomer;
import com.allane.models.dto.contract.FrontendLeasingVehicle;
import com.allane.models.dto.vehicle.FrontendVehicle;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;

public class TestUtil {

    public static Customer createCustomer(long randomId) {
        Customer customer = new Customer();
        customer.setId(randomId);
        customer.setFirstName("Max" + randomId);
        customer.setLastName("Mustermann" + randomId);
        customer.setBirthDate(LocalDate.now().minusYears(30));
        return customer;
    }

    public static Vehicle createVehicle(long randomId, double price, boolean setVin) {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(randomId);
        vehicle.setBrand("BMW");
        vehicle.setPrice(price);
        vehicle.setModel("model" + randomId);
        vehicle.setModelYear(Year.of(1992));
        vehicle.setVin(setVin ? "vin" + randomId : null);
        return vehicle;
    }

    public static LeasingContract createContract(long id, Customer customer, Vehicle vehicle, double monthlyRate) {
        LeasingContract leasingContract = new LeasingContract();
        leasingContract.setContractNumber(id);
        leasingContract.setVehicle(vehicle);
        leasingContract.setCustomer(customer);
        leasingContract.setMonthlyRate(monthlyRate);
        return leasingContract;
    }

    public static FrontendLeasingContract createFrontendLeasingContract(LeasingContract leasingContract) {
        return FrontendLeasingContract.builder()
                .contractNumber(leasingContract.getContractNumber())
                .customer(createFrontendLeasingCustomer(leasingContract.getCustomer()))
                .monthlyRate(leasingContract.getMonthlyRate())
                .vehicle(createFrontendLeasingVehicle(leasingContract.getVehicle()))
                .build();
    }

    public static FrontendLeasingVehicle createFrontendLeasingVehicle(Vehicle vehicle) {
        return FrontendLeasingVehicle.builder()
                .id(vehicle.getId())
                .vehicleModel(vehicle.getVehicleModel())
                .vin(vehicle.getVin())
                .price(vehicle.getPrice())
                .build();
    }

    public static FrontendLeasingCustomer createFrontendLeasingCustomer(Customer customer) {
        return FrontendLeasingCustomer.builder()
                .id(customer.getId())
                .fullName(customer.getFullName())
                .fullNameWithBirthDate(customer.getFullName() +
                        " (" + customer.getBirthDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + ")")
                .build();
    }

    public static FrontendVehicle createFrontendVehicle(Vehicle vehicle) {
        return FrontendVehicle.builder()
                .id(vehicle.getId())
                .modelYear(vehicle.getModelYear())
                .model(vehicle.getModel())
                .brand(vehicle.getBrand())
                .price(vehicle.getPrice())
                .vin(vehicle.getVin())
                .build();
    }

    public static FrontendCustomer createFrontendCustomer(Customer customer) {
        return FrontendCustomer.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .birthDate(customer.getBirthDate())
                .build();
    }


}
