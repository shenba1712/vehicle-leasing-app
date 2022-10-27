package com.allane.service.mapping;

import com.allane.models.Customer;
import com.allane.models.dto.customer.FrontendCustomer;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import static com.allane.TestUtil.createCustomer;
import static com.allane.TestUtil.createFrontendCustomer;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class CustomerMappingServiceTest {
    @InjectMocks
    private CustomerMappingService mappingService;

    @Test
    void mapToFrontendCustomer() {
        Customer customer = createCustomer(1);
        FrontendCustomer frontendCustomer = createFrontendCustomer(customer);

        FrontendCustomer actualCustomer = mappingService.mapToFrontendCustomer(customer);

        assertThat(actualCustomer).usingRecursiveComparison().isEqualTo(frontendCustomer);
    }
}
