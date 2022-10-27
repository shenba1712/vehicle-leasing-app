package com.allane.models.dto.contract;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FrontendLeasingCustomer {
    long id;
    String fullName;
    String fullNameWithBirthDate;
}
