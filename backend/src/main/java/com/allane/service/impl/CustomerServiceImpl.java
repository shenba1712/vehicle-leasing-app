package com.allane.service.impl;

import com.allane.models.Customer;
import com.allane.models.dto.customer.NewCustomer;
import com.allane.models.dto.customer.FrontendCustomer;
import com.allane.models.dto.customer.FrontendCustomers;
import com.allane.repository.CustomerRepository;
import com.allane.service.CustomerService;
import com.allane.service.mapping.CustomerMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    static final int MAX_PAGE_SIZE = 50;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMappingService mappingService;

    @Override
    public FrontendCustomer getCustomerDetails(long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        return mappingService.mapToFrontendCustomer(customer);
    }

    @Override
    public void addCustomer(NewCustomer newCustomer) {
        Customer customer = new Customer();
        customer.setFirstName(newCustomer.getFirstName());
        customer.setLastName(newCustomer.getLastName());
        customer.setBirthDate(newCustomer.getBirthDate());
        customerRepository.save(customer);
    }

    @Override
    public void updateCustomerDetails(long id, NewCustomer updatedCustomerDetails) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find customer with id: " + id));
        customer.setBirthDate(updatedCustomerDetails.getBirthDate());
        customer.setFirstName(updatedCustomerDetails.getFirstName());
        customer.setLastName(updatedCustomerDetails.getLastName());
        customerRepository.save(customer);
    }

    @Override
    public FrontendCustomers getCustomers(int pageNumber) {
        FrontendCustomers customers = new FrontendCustomers();
        Page<Customer> customerPage  = customerRepository.findAll(PageRequest.of(pageNumber, MAX_PAGE_SIZE));
        customers.setTotalResults(customerPage.getTotalElements());
        List<FrontendCustomer> customerList = customerPage.getContent().stream().map(mappingService::mapToFrontendCustomer).collect(Collectors.toList());
        customers.setCustomers(customerList);
        return customers;
    }
}
