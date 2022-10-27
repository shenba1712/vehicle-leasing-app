package com.allane.models.dto.contract;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class NewContract {
    @NotNull
    double monthlyRate;
    @NotNull
    long customerId;
    @NotNull
    long vehicleId;
}
