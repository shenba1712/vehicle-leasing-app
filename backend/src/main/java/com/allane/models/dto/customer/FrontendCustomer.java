package com.allane.models.dto.customer;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class FrontendCustomer {
    Long id;
    String firstName;
    String lastName;
    LocalDate birthDate;
}
