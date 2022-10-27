package com.allane.controller;

import com.allane.models.Customer;
import com.allane.models.dto.customer.NewCustomer;
import com.allane.models.dto.customer.FrontendCustomer;
import com.allane.models.dto.customer.FrontendCustomers;
import com.allane.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static com.allane.TestUtil.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @MockBean
    private CustomerService customerService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getAllCustomers() throws Exception {
        Customer customer1 = createCustomer(1);
        FrontendCustomer frontendCustomer1 = createFrontendCustomer(customer1);
        Customer customer2 = createCustomer(2);
        FrontendCustomer frontendCustomer2 = createFrontendCustomer(customer2);
        Customer customer3 = createCustomer(3);
        FrontendCustomer frontendCustomer3 = createFrontendCustomer(customer3);

        FrontendCustomers frontendCustomers = new FrontendCustomers();
        frontendCustomers.setTotalResults(25L);
        frontendCustomers.setCustomers(List.of(frontendCustomer1, frontendCustomer2, frontendCustomer3));

        when(customerService.getCustomers(anyInt())).thenReturn(frontendCustomers);

        mockMvc.perform(get("/customer/page/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalResults", Matchers.is(25)))
                .andExpect(jsonPath("$.customers", Matchers.hasSize(3)))

                .andExpect(jsonPath("$.customers[0].firstName", Matchers.is(frontendCustomer1.getFirstName())))
                .andExpect(jsonPath("$.customers[0].lastName", Matchers.is(frontendCustomer1.getLastName())))
                .andExpect(jsonPath("$.customers[0].birthDate", Matchers.is(frontendCustomer1.getBirthDate().toString())))

                .andExpect(jsonPath("$.customers[1].firstName", Matchers.is(frontendCustomer2.getFirstName())))
                .andExpect(jsonPath("$.customers[1].lastName", Matchers.is(frontendCustomer2.getLastName())))
                .andExpect(jsonPath("$.customers[1].birthDate", Matchers.is(frontendCustomer2.getBirthDate().toString())))


                .andExpect(jsonPath("$.customers[2].firstName", Matchers.is(frontendCustomer3.getFirstName())))
                .andExpect(jsonPath("$.customers[2].lastName", Matchers.is(frontendCustomer3.getLastName())))
                .andExpect(jsonPath("$.customers[2].birthDate", Matchers.is(frontendCustomer3.getBirthDate().toString())));

    }

    @Nested
    public class GetCustomerDetails {
        @Test
        public void notFound() throws Exception {
            when(customerService.getCustomerDetails(anyLong())).thenThrow(new IllegalArgumentException());

            mockMvc.perform(get("/customer/1222"))
                    .andExpect(status().isNotFound());
        }

        @Test
        public void randomError() throws Exception {
            when(customerService.getCustomerDetails(anyLong())).thenThrow(new RuntimeException("Random error"));

            mockMvc.perform(get("/customer/1222"))
                    .andExpect(status().isInternalServerError());
        }

        @Test
        public void success() throws Exception {
            Customer customer = createCustomer(1);
            FrontendCustomer frontendCustomer = createFrontendCustomer(customer);
            when(customerService.getCustomerDetails(1L)).thenReturn(frontendCustomer);

            mockMvc.perform(get("/customer/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", Matchers.is(1)))
                    .andExpect(jsonPath("$.firstName", Matchers.is("Max1")))
                    .andExpect(jsonPath("$.lastName", Matchers.is("Mustermann1")))
                    .andExpect(jsonPath("$.birthDate", Matchers.is(LocalDate.now().minusYears(30).toString())));
        }
    }

    @Nested
    public class AddCustomer {
        NewCustomer newCustomer;

        @BeforeEach
        public void setUp() {
            newCustomer = new NewCustomer();
            newCustomer.setBirthDate(LocalDate.now().minusYears(20));
            newCustomer.setLastName("Leach");
            newCustomer.setFirstName("Alia");
        }

        @Test
        public void randomError() throws Exception {
            doThrow(new RuntimeException("Random error")).when(customerService).addCustomer(any(NewCustomer.class));

            mockMvc.perform(post("/customer")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(newCustomer)))
                    .andExpect(status().isInternalServerError());
        }

        @Test
        public void success() throws Exception {
            mockMvc.perform(post("/customer")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(newCustomer)))
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    public class UpdateCustomerDetails {
        NewCustomer updateCustomer;

        @BeforeEach
        public void setUp() {
            updateCustomer = new NewCustomer();
            updateCustomer.setBirthDate(LocalDate.now().minusYears(20));
            updateCustomer.setLastName("Leach");
            updateCustomer.setFirstName("Alia");
        }

        @Test
        public void illegalArguments() throws Exception {
            doThrow(new IllegalArgumentException()).when(customerService).updateCustomerDetails(anyLong(), any(NewCustomer.class));

            mockMvc.perform(put("/customer/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateCustomer)))
                    .andExpect(status().isUnprocessableEntity());
        }

        @Test
        public void randomError() throws Exception {
            doThrow(new RuntimeException("Random error")).when(customerService)
                    .updateCustomerDetails(anyLong(), any(NewCustomer.class));

            mockMvc.perform(put("/customer/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateCustomer)))
                    .andExpect(status().isInternalServerError());
        }

        @Test
        public void success() throws Exception {
            mockMvc.perform(put("/customer/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateCustomer)))
                    .andExpect(status().isNoContent());
        }
    }
}
