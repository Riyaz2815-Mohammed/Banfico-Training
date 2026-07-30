package com.riyaz.banficotrainingprogram.Service.impl;

import com.riyaz.banficotrainingprogram.Entity.Account;
import com.riyaz.banficotrainingprogram.Entity.Customer;
import com.riyaz.banficotrainingprogram.Service.AccountService;
import com.riyaz.banficotrainingprogram.dto.AccountRequest;
import com.riyaz.banficotrainingprogram.dto.AccountResponse;
import com.riyaz.banficotrainingprogram.repository.AccountRepo;
import com.riyaz.banficotrainingprogram.repository.CustomerRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepo accountRepo;
    private final CustomerRepo customerRepo;

    public AccountServiceImpl(AccountRepo accountRepo, CustomerRepo customerRepo) {
        this.accountRepo = accountRepo;
        this.customerRepo = customerRepo;
    }

    @Override
    public AccountResponse createAccount(AccountRequest accountRequest) {
        Customer customer = customerRepo.findById(accountRequest.getCustomerId()).orElse(null);
        Account account = new Account(accountRequest.getAccountNo(),accountRequest.getAccountType(),accountRequest.getBalance(),customer);
        Account saveaccount = accountRepo.save(account);
        return new AccountResponse(saveaccount.getId(),saveaccount.getAccountNo(),saveaccount.getAccountType(),saveaccount.getBalance(),saveaccount.getCustomer().getId());
    }

    @Override
    public List<AccountResponse> getAccounts() {
        List<Account> accounts = accountRepo.findAll();
        return accounts.stream().map(account -> new AccountResponse(
                account.getId(),account.getAccountNo(),account.getAccountType(),
                account.getBalance(),account.getCustomer().getId()
        )).toList();
    }

    @Override
    public AccountResponse getAccount(UUID accountId) {
        Account account = accountRepo.findById(accountId).orElse(null);
        if  (account == null) {return null;}
        return new AccountResponse(account.getId(),account.getAccountNo(),account.getAccountType(),account.getBalance(), account.getCustomer().getId());
    }

    @Override
    public AccountResponse updateAccount(UUID id,AccountRequest accountRequest) {
        Account account = accountRepo.findById(id).orElse(null);
        if  (account == null) {return null;}
//        account.setAccountNo(accountRequest.getAccountNo());
        account.setAccountType(accountRequest.getAccountType());
        account.setBalance(accountRequest.getBalance());
        Account updatedAccount = accountRepo.save(account);
        return new AccountResponse(
                updatedAccount.getId(),
                updatedAccount.getAccountNo(),
                updatedAccount.getAccountType(),
                updatedAccount.getBalance(),
                updatedAccount.getCustomer().getId()
        );
    }

    @Override
    public void deleteAccount(UUID accountId) {
        accountRepo.deleteById(accountId);
    }


}
