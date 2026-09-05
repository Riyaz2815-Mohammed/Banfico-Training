package com.riyaz.banficotrainingprogram.repository;

import com.riyaz.banficotrainingprogram.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepo extends JpaRepository<Account, UUID> {
    List<Account> findByCustomerId(UUID customerId);
    Optional<Account> findByAccountNo(String accountNo);
}
