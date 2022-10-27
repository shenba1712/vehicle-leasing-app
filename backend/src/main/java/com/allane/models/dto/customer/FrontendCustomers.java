package com.allane.models.dto.customer;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FrontendCustomers {
    private long totalResults;
    private List<FrontendCustomer> customers;
}
