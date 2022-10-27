package com.allane.service.impl;

import com.allane.models.Customer;
import com.allane.models.dto.customer.NewCustomer;
import com.allane.models.dto.customer.FrontendCustomer;
import com.allane.models.dto.customer.FrontendCustomers;
import com.allane.repository.CustomerRepository;
import com.allane.service.mapping.CustomerMappingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.allane.TestUtil.createCustomer;
import static com.allane.TestUtil.createFrontendCustomer;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CustomerServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMappingService mappingService;

    @InjectMocks
    private CustomerServiceImpl service;

    @Test
    public void getAllCustomersTest() {
        // Prepare
        Customer customer1 = createCustomer(1);
        Customer customer2 = createCustomer(2);
        Customer customer3 = createCustomer(3);

        Page<Customer> page = new PageImpl<>(List.of(customer1, customer2, customer3));
        when(customerRepository.findAll(PageRequest.of(1, 50))).thenReturn(page);
        when(mappingService.mapToFrontendCustomer(any(Customer.class))).thenCallRealMethod();

        FrontendCustomer frontendCustomer1 = createFrontendCustomer(customer1);
        FrontendCustomer frontendCustomer2 = createFrontendCustomer(customer2);
        FrontendCustomer frontendCustomer3 = createFrontendCustomer(customer3);

        FrontendCustomers frontendCustomers = new FrontendCustomers();
        frontendCustomers.setTotalResults(3);
        frontendCustomers.setCustomers(List.of(frontendCustomer1, frontendCustomer2, frontendCustomer3));

        //Act
        FrontendCustomers actualCustomers = service.getCustomers(1);

        // Assert
        verify(customerRepository, times(1)).findAll(any(Pageable.class));
        verify(mappingService, times(3)).mapToFrontendCustomer(any(Customer.class));
        assertEquals(3, actualCustomers.getCustomers().size());
        assertThat(frontendCustomer1).usingRecursiveComparison().isEqualTo(actualCustomers.getCustomers().get(0));
        assertThat(frontendCustomer2).usingRecursiveComparison().isEqualTo(actualCustomers.getCustomers().get(1));
        assertThat(frontendCustomer3).usingRecursiveComparison().isEqualTo(actualCustomers.getCustomers().get(2));
    }

    @Nested
    public class GetCustomerDetails {

        @Test
        public void notFound() {
            when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                service.getCustomerDetails(1);
            });

            verify(mappingService, never()).mapToFrontendCustomer(any(Customer.class));
        }

        @Test
        public void success() {
            Customer customer = createCustomer(1);
            when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
            when(mappingService.mapToFrontendCustomer(customer)).thenCallRealMethod();

            FrontendCustomer frontendCustomer = createFrontendCustomer(customer);

            FrontendCustomer actualCustomer = service.getCustomerDetails(1L);

            verify(customerRepository).findById(1L);
            verify(mappingService).mapToFrontendCustomer(customer);

            assertThat(actualCustomer).usingRecursiveComparison().isEqualTo(frontendCustomer);
        }
    }

    @Nested
    public class AddCustomer {
        NewCustomer newCustomer;

        @BeforeEach
        public void setUp() {
            newCustomer = new NewCustomer();
            newCustomer.setFirstName("Erica");
            newCustomer.setLastName("Leach");
            newCustomer.setBirthDate(LocalDate.now().minusYears(15));
        }

        @Test
        public void success() {
            service.addCustomer(newCustomer);

            ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);
            verify(customerRepository).save(captor.capture());
            Customer customer = captor.getValue();
            assertThat(customer.getFirstName()).isEqualTo(newCustomer.getFirstName());
            assertThat(customer.getLastName()).isEqualTo(newCustomer.getLastName());
            assertThat(customer.getBirthDate()).isEqualTo(newCustomer.getBirthDate());
        }
    }

    @Nested
    public class UpdateCustomerDetails {
        NewCustomer updatedCustomer;

        @BeforeEach
        public void setUp() {
            updatedCustomer = new NewCustomer();
            updatedCustomer.setFirstName("Erica");
            updatedCustomer.setLastName("Leach");
            updatedCustomer.setBirthDate(LocalDate.now().minusYears(15));

            Customer customer = createCustomer(1);
            when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        }

        @Test
        public void customerNotFound() {
            when(customerRepository.findById(1L)).thenReturn(Optional.empty());

            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                service.updateCustomerDetails(1L, updatedCustomer);
            });

            verify(customerRepository, never()).save(any(Customer.class));
        }

        @Test
        public void success() {
            service.updateCustomerDetails(1L, updatedCustomer);

            ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);
            verify(customerRepository).save(captor.capture());
            Customer customer = captor.getValue();
            assertThat(customer.getId()).isEqualTo(1L);
            assertThat(customer.getFirstName()).isEqualTo(updatedCustomer.getFirstName());
            assertThat(customer.getLastName()).isEqualTo(updatedCustomer.getLastName());
            assertThat(customer.getBirthDate()).isEqualTo(updatedCustomer.getBirthDate());
        }
    }
}
