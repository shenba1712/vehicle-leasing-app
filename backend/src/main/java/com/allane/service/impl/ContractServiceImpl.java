package com.allane.service.impl;

import com.allane.models.dto.contract.NewContract;
import com.allane.models.Customer;
import com.allane.models.LeasingContract;
import com.allane.models.Vehicle;
import com.allane.models.dto.contract.FrontendLeasingContract;
import com.allane.models.dto.contract.FrontendLeasingContracts;
import com.allane.models.dto.contract.FrontendLeasingCustomer;
import com.allane.models.dto.contract.FrontendLeasingVehicle;
import com.allane.repository.CustomerRepository;
import com.allane.repository.ContractRepository;
import com.allane.repository.VehicleRepository;
import com.allane.service.mapping.ContractMappingService;
import com.allane.service.ContractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Slf4j
@Service
public class ContractServiceImpl implements ContractService {

    static final int MAX_PAGE_SIZE = 50;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ContractMappingService mappingService;

    @Override
    public FrontendLeasingContracts getAllContracts(int pageNumber) {
        FrontendLeasingContracts contracts = new FrontendLeasingContracts();
        Page<LeasingContract> leasingContractPage =  contractRepository.findAll(PageRequest.of(pageNumber, MAX_PAGE_SIZE));
        contracts.setTotalResults(leasingContractPage.getTotalElements());
        List<FrontendLeasingContract> contractList = leasingContractPage.getContent().stream()
                .map(mappingService::mapToFrontendLeasingContract).collect(Collectors.toList());
        contracts.setContracts(contractList);
        return contracts;
    }

    @Override
    public FrontendLeasingContract getContract(long id) {
        LeasingContract leasingContract = contractRepository.findByContractNumber(id)
                .orElseThrow(IllegalArgumentException::new);
        return mappingService.mapToFrontendLeasingContract(leasingContract);
    }

    @Override
    public void registerContract(NewContract newContract) {
        Customer customer = customerRepository.findById(newContract.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Cannot find customer of id: " + newContract.getCustomerId()));
        Vehicle vehicle = vehicleRepository.findById(newContract.getVehicleId())
                .orElseThrow(() -> new IllegalArgumentException("Cannot find vehicle of id: " + newContract.getVehicleId()));

        // If vehicle already has a contract, then throw error.
        if (nonNull(vehicle.getLeasingContract())) {
            throw new IllegalStateException("Cannot create contact because an active contract already exists with this vehicle id: "
                    + newContract.getVehicleId());
        }

        LeasingContract contract = new LeasingContract();
        contract.setCustomer(customer);
        contract.setVehicle(vehicle);
        contract.setMonthlyRate(newContract.getMonthlyRate());

        contractRepository.save(contract);
    }

    @Override
    public void updateContract(long id, NewContract updatedContract) {
        LeasingContract contract = contractRepository.findByContractNumber(id)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find contract of id: " + id));

        if (contract.getMonthlyRate() != updatedContract.getMonthlyRate()) {
            contract.setMonthlyRate(updatedContract.getMonthlyRate());
        }

        if (contract.getCustomer().getId() != updatedContract.getCustomerId()) {
            Customer customer = customerRepository.findById(updatedContract.getCustomerId())
                    .orElseThrow(() -> new IllegalArgumentException("Cannot find customer of id: " + updatedContract.getCustomerId()));
            contract.setCustomer(customer);
        }

        if (contract.getVehicle().getId() != updatedContract.getVehicleId()) {
            Vehicle vehicle = vehicleRepository.findById(updatedContract.getVehicleId())
                    .orElseThrow(() -> new IllegalArgumentException("Cannot find vehicle of id: " + updatedContract.getVehicleId()));
            contract.setVehicle(vehicle);
        }

        contractRepository.save(contract);
    }

    @Override
    public List<FrontendLeasingCustomer> filterCustomers(String query) {
        if (isNull(query)) {
            query = "";
        }
        return customerRepository.findTop10ByLastNameStartingWithIgnoreCase(query).stream()
                .map(mappingService::mapToFrontendLeasingCustomer)
                .collect(Collectors.toList());
    }

    @Override
    public List<FrontendLeasingVehicle> filterVehicles(String brandQuery) {
        if (isNull(brandQuery)) {
            brandQuery = "";
        }
        // filter out vehicles already leased
        return vehicleRepository.findTop10ByBrandIgnoreCaseStartingWith(brandQuery)
                .stream()
                .filter(vehicle -> isNull(vehicle.getLeasingContract()))
                .map(mappingService::mapToFrontendVehicle)
                .collect(Collectors.toList());
    }
}
