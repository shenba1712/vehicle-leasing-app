package com.allane.controller;

import com.allane.models.dto.customer.NewCustomer;
import com.allane.models.dto.customer.FrontendCustomer;
import com.allane.models.dto.customer.FrontendCustomers;
import com.allane.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.*;

@RestController
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/page/{pageNumber}")
    public FrontendCustomers getAllCustomers(@PathVariable int pageNumber) {
        return customerService.getCustomers(pageNumber);
    }

    @GetMapping("{id}")
    public ResponseEntity<FrontendCustomer> getCustomer(@PathVariable long id) {
        try {
            return ResponseEntity.ok(customerService.getCustomerDetails(id));
        } catch (IllegalArgumentException e) {
            log.error("Unable to find customer with id: " + id, e);
            return new ResponseEntity<>(NOT_FOUND);
        } catch (Exception e) {
            log.error("Unexpected error. Cannot find customer with id: " + id, e);
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Void> addCustomer(@Valid @RequestBody NewCustomer newCustomer) {
        try {
            customerService.addCustomer(newCustomer);
            return new ResponseEntity<>(NO_CONTENT);
        } catch (Exception e) {
            log.error("Unexpected error. Cannot add new customer", e);
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateCustomerDetails(@PathVariable long id, @Valid @RequestBody NewCustomer updatedCustomer) {
        try {
            customerService.updateCustomerDetails(id, updatedCustomer);
            return new ResponseEntity<>(NO_CONTENT);
        } catch (IllegalArgumentException e) {
            log.error("Cannot update customer", e);
            return new ResponseEntity<>(UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            log.error("Unexpected error. Cannot update customer", e);
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
    }
}
