package com.allane.models.dto.contract;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FrontendLeasingContract {
    long contractNumber;
    private double monthlyRate;
    FrontendLeasingCustomer customer;
    FrontendLeasingVehicle vehicle;
}
