package com.riyaz.banficotrainingprogram.repository;

import com.riyaz.banficotrainingprogram.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface AccountRepo extends JpaRepository<Account, UUID> {
}
