package com.allane.models.dto.contract;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FrontendLeasingContracts {
    long totalResults;
    List<FrontendLeasingContract> contracts;
}
