package com.allane.service;

import com.allane.models.dto.customer.NewCustomer;
import com.allane.models.dto.customer.FrontendCustomer;
import com.allane.models.dto.customer.FrontendCustomers;

public interface CustomerService {
    FrontendCustomer getCustomerDetails(long id);

    void addCustomer(NewCustomer newCustomer);

    void updateCustomerDetails(long id, NewCustomer updatedCustomerDetails);

    FrontendCustomers getCustomers(int pageNumber);
}
