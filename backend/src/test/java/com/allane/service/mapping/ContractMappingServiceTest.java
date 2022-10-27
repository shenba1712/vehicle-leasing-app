package com.allane.service.mapping;

import com.allane.models.Customer;
import com.allane.models.LeasingContract;
import com.allane.models.Vehicle;
import com.allane.models.dto.contract.FrontendLeasingContract;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import static com.allane.TestUtil.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class ContractMappingServiceTest {

    @InjectMocks
    ContractMappingService mappingService;

    @Test
    public void mapToFrontendLeasingContractTest() {
        Customer customer = createCustomer(1);
        Vehicle vehicle = createVehicle(1, 10000, true);
        LeasingContract contract = createContract(1, customer, vehicle, 450);
        FrontendLeasingContract frontendLeasingContract = createFrontendLeasingContract(contract);

        FrontendLeasingContract actualContract = mappingService.mapToFrontendLeasingContract(contract);

        assertThat(actualContract).usingRecursiveComparison().isEqualTo(frontendLeasingContract);
    }

}
