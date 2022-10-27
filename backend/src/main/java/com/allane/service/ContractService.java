package com.allane.service;

import com.allane.models.dto.contract.NewContract;
import com.allane.models.dto.contract.FrontendLeasingContract;
import com.allane.models.dto.contract.FrontendLeasingContracts;
import com.allane.models.dto.contract.FrontendLeasingCustomer;
import com.allane.models.dto.contract.FrontendLeasingVehicle;

import java.util.List;

public interface ContractService {

    FrontendLeasingContracts getAllContracts(int pageNumber);

    FrontendLeasingContract getContract(long id);

    void registerContract(NewContract newContract);

    void updateContract(long id, NewContract updatedContract);

    List<FrontendLeasingCustomer> filterCustomers(String query);

    List<FrontendLeasingVehicle> filterVehicles(String brandQuery);
}
