package com.riyaz.banficotrainingprogram.Service.impl;

import com.riyaz.banficotrainingprogram.Entity.Account;
import com.riyaz.banficotrainingprogram.Entity.Customer;
import com.riyaz.banficotrainingprogram.Service.AccountService;
import com.riyaz.banficotrainingprogram.dto.AccountLookupResponse;
import com.riyaz.banficotrainingprogram.dto.AccountRequest;
import com.riyaz.banficotrainingprogram.dto.AccountResponse;
import com.riyaz.banficotrainingprogram.exception.ResourceNotFoundException;
import com.riyaz.banficotrainingprogram.repository.AccountRepo;
import com.riyaz.banficotrainingprogram.repository.BeneficiaryRepo;
import com.riyaz.banficotrainingprogram.repository.CustomerRepo;
import com.riyaz.banficotrainingprogram.repository.TransactionsRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepo accountRepo;
    private final CustomerRepo customerRepo;
    private final TransactionsRepo transactionsRepo;
    private final BeneficiaryRepo beneficiaryRepo;

    public AccountServiceImpl(AccountRepo accountRepo, CustomerRepo customerRepo, TransactionsRepo transactionsRepo, BeneficiaryRepo beneficiaryRepo) {
        this.accountRepo = accountRepo;
        this.customerRepo = customerRepo;
        this.transactionsRepo = transactionsRepo;
        this.beneficiaryRepo = beneficiaryRepo;
    }

    @Override
    public AccountResponse createAccount(AccountRequest accountRequest) {
        Customer customer = customerRepo.findById(accountRequest.getCustomerId()).orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + accountRequest.getCustomerId()));
        Account savedAccount = accountRepo.save(new Account(accountRequest.getAccountNo(), accountRequest.getAccountType(), accountRequest.getBalance(), customer));
        return new AccountResponse(savedAccount.getId(), savedAccount.getAccountNo(), savedAccount.getAccountType(), savedAccount.getBalance(), savedAccount.getCustomer().getId(), savedAccount.getCustomer().getFirstName() + " " + savedAccount.getCustomer().getLastName());
    }

    @Override
    public List<AccountResponse> getAccounts() {
        return accountRepo.findAll().stream().map(account -> new AccountResponse(
                account.getId(), account.getAccountNo(), account.getAccountType(),
                account.getBalance(), account.getCustomer().getId(),
                account.getCustomer().getFirstName() + " " + account.getCustomer().getLastName()
        )).toList();
    }

    @Override
    public List<AccountResponse> getMyAccounts(String email) {
        Customer customer = customerRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("No customer record found for email: " + email));
        return accountRepo.findByCustomerId(customer.getId()).stream().map(account -> new AccountResponse(
                account.getId(), account.getAccountNo(), account.getAccountType(),
                account.getBalance(), account.getCustomer().getId(),
                account.getCustomer().getFirstName() + " " + account.getCustomer().getLastName()
        )).toList();
    }

    @Override
    public AccountResponse getAccount(UUID accountId) {
        Account account = accountRepo.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));
        return new AccountResponse(account.getId(), account.getAccountNo(), account.getAccountType(), account.getBalance(), account.getCustomer().getId(), account.getCustomer().getFirstName() + " " + account.getCustomer().getLastName());
    }

    @Override
    public AccountResponse updateAccount(UUID id, AccountRequest accountRequest) {
        Account account = accountRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
        account.setAccountType(accountRequest.getAccountType());
        account.setBalance(accountRequest.getBalance());
        Account updatedAccount = accountRepo.save(account);
        return new AccountResponse(updatedAccount.getId(), updatedAccount.getAccountNo(), updatedAccount.getAccountType(), updatedAccount.getBalance(), updatedAccount.getCustomer().getId(), updatedAccount.getCustomer().getFirstName() + " " + updatedAccount.getCustomer().getLastName());
    }

    @Override
    @Transactional
    public void deleteAccount(UUID accountId) {
        accountRepo.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));
        beneficiaryRepo.deleteAll(beneficiaryRepo.findByBeneficiaryAccountId(accountId));
        transactionsRepo.deleteAll(transactionsRepo.findByAccountIdOrderByTransactionTimeDesc(accountId));
        accountRepo.deleteById(accountId);
    }

    @Override
    public AccountLookupResponse lookupByAccountNo(String accountNo) {
        Account account = accountRepo.findByAccountNo(accountNo).orElseThrow(() -> new ResourceNotFoundException("No account found with number: " + accountNo));
        return new AccountLookupResponse(account.getId(), account.getAccountNo(), account.getAccountType(), account.getCustomer().getFirstName() + " " + account.getCustomer().getLastName());
    }
}
