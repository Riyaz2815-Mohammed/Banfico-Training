package com.riyaz.banficotrainingprogram.Service.impl;

import com.riyaz.banficotrainingprogram.Entity.Account;
import com.riyaz.banficotrainingprogram.Service.AccountService;
import com.riyaz.banficotrainingprogram.dto.AccountRequest;
import com.riyaz.banficotrainingprogram.dto.AccountResponse;
import com.riyaz.banficotrainingprogram.repository.AccountRepo;

import java.util.List;
import java.util.UUID;

public class AccountServiceImpl implements AccountService {
    private final AccountRepo accountRepo;
    public AccountServiceImpl(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }

    @Override
    public AccountResponse createAccount(AccountRequest accountRequest) {
        return null;
    }

    @Override
    public List<AccountResponse> getAccounts() {
        return List.of();
    }

    @Override
    public AccountResponse getAccount(UUID accountId) {
        Account account = accountRepo.findById(accountId).orElse(null);
        if  (account == null) {return null;}
        return new AccountResponse(account.getId(),account.getAccountNo(),account.getAccountType(),account.getBalance(), account.getCustomer().getId());
    }

    @Override
    public AccountRequest updateAccount(UUID id,AccountRequest accountRequest) {
        return null;
    }

    @Override
    public void deleteAccount(UUID accountId) {

    }


}
