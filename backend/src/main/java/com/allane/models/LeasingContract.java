package com.allane.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "leasing_Contract")
@Getter
@Setter
public class LeasingContract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_number")
    private Long contractNumber;

    private double monthlyRate;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @JoinColumn(name = "vehicle_id")
    @OneToOne
    private Vehicle vehicle;
}
