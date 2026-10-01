package com.riyaz.banficotrainingprogram.transaction.service.impl;

import com.riyaz.banficotrainingprogram.account.repository.AccountRepo;
import com.riyaz.banficotrainingprogram.customer.entity.Customer;
import com.riyaz.banficotrainingprogram.customer.repository.CustomerRepo;
import com.riyaz.banficotrainingprogram.exception.ResourceNotFoundException;
import com.riyaz.banficotrainingprogram.transaction.dto.TransactionResponse;
import com.riyaz.banficotrainingprogram.transaction.entity.Transactions;
import com.riyaz.banficotrainingprogram.transaction.repository.TransactionsRepo;
import com.riyaz.banficotrainingprogram.transaction.service.TransactionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionsRepo transactionsRepo;
    private final AccountRepo accountRepo;
    private final CustomerRepo customerRepo;

    public TransactionServiceImpl(TransactionsRepo transactionsRepo, AccountRepo accountRepo, CustomerRepo customerRepo) {
        this.transactionsRepo = transactionsRepo;
        this.accountRepo = accountRepo;
        this.customerRepo = customerRepo;
    }

    @Override
    public List<TransactionResponse> getTransactions(UUID accountId, String email, boolean isStaff) {
        if (!isStaff) {
            Customer customer = customerRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("No customer record found for email: " + email));
            Account account = accountRepo.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));
            if (!account.getCustomer().getId().equals(customer.getId())) throw new ResourceNotFoundException("Account not found with id: " + accountId);
        }
        return transactionsRepo.findByAccountIdOrderByTransactionTimeDesc(accountId).stream().map(t -> new TransactionResponse(t.getId(), t.getType(), t.getAccount().getId(), t.getBalanceAfter(), t.getAmount(), t.getTransactionTime(), t.getDescription())).toList();
    }
}
