package com.allane.controller;

import com.allane.models.dto.contract.NewContract;
import com.allane.models.dto.contract.FrontendLeasingContract;
import com.allane.models.dto.contract.FrontendLeasingContracts;
import com.allane.models.dto.contract.FrontendLeasingCustomer;
import com.allane.models.dto.contract.FrontendLeasingVehicle;
import com.allane.service.ContractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
@Slf4j
@RequestMapping("contract")
public class ContractController {

    @Autowired
    private ContractService contractService;

    @GetMapping("/overview/{pageNumber}")
    public FrontendLeasingContracts getAllContracts(@PathVariable int pageNumber) {
        return contractService.getAllContracts(pageNumber);
    }

    @GetMapping("{id}")
    public ResponseEntity<FrontendLeasingContract> getContractById(@PathVariable long id) {
        try {
            return ResponseEntity.ok(contractService.getContract(id));
        } catch (IllegalArgumentException e) {
            log.error("Unable to find contract with id: " + id, e);
            return new ResponseEntity<>(NOT_FOUND);
        } catch (Exception e) {
            log.error("Unexpected error. Cannot find contract with id: " + id, e);
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Void> addContract(@Valid @RequestBody NewContract newContract) {
        try {
            contractService.registerContract(newContract);
            return new ResponseEntity<>(NO_CONTENT);
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("Cannot add new contract", e);
            return new ResponseEntity<>(UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            log.error("Unexpected error. Cannot add new contract", e);
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> editContract(@PathVariable long id, @Valid @RequestBody NewContract updatedContract) {
        try {
            contractService.updateContract(id, updatedContract);
            return new ResponseEntity<>(NO_CONTENT);
        } catch (IllegalArgumentException e) {
            log.error("Cannot update contract", e);
            return new ResponseEntity<>(UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            log.error("Unexpected error. Cannot update contract", e);
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/filter/customers")
    public List<FrontendLeasingCustomer> filterCustomers(@RequestParam(required = false) String query) {
        return contractService.filterCustomers(query);
    }

    @GetMapping("/filter/vehicles")
    public List<FrontendLeasingVehicle> filterVehicles(@RequestParam(required = false) String brandQuery) {
        return contractService.filterVehicles(brandQuery);
    }

}
