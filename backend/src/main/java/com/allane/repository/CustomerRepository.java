package com.allane.repository;

import com.allane.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findTop10ByLastNameStartingWithIgnoreCase(String query);
}
