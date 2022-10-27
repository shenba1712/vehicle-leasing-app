package com.allane.service.mapping;

import com.allane.models.Customer;
import com.allane.models.dto.customer.FrontendCustomer;
import org.springframework.stereotype.Service;

@Service
public class CustomerMappingService {

    public FrontendCustomer mapToFrontendCustomer(Customer customer) {
        return FrontendCustomer.builder()
                .id(customer.getId())
                .birthDate(customer.getBirthDate())
                .lastName(customer.getLastName())
                .firstName(customer.getFirstName())
                .build();
    }
}
