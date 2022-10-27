package com.allane.repository;

import com.allane.models.LeasingContract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContractRepository extends JpaRepository<LeasingContract, Long> {

    Page<LeasingContract> findAll(Pageable pageable);

    Optional<LeasingContract> findByContractNumber(Long contractNumber);
}
