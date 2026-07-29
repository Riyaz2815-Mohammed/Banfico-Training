package com.riyaz.banficotrainingprogram.repository;

import com.riyaz.banficotrainingprogram.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepo extends JpaRepository<Customer, UUID> {

}
