package com.riyaz.banficotrainingprogram.Service.impl;

import com.riyaz.banficotrainingprogram.Entity.Account;
import com.riyaz.banficotrainingprogram.Entity.Transactions;
import com.riyaz.banficotrainingprogram.Service.TransactionService;
import com.riyaz.banficotrainingprogram.dto.TransactionRequest;
import com.riyaz.banficotrainingprogram.dto.TransactionResponse;
import com.riyaz.banficotrainingprogram.repository.AccountRepo;
import com.riyaz.banficotrainingprogram.repository.TransactionsRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TransactionServiceImpl implements TransactionService {
    private final TransactionsRepo transactionsRepo;
    private final AccountRepo accountRepo;

    public TransactionServiceImpl(TransactionsRepo transactionsRepo, AccountRepo accountRepo) {
        this.transactionsRepo = transactionsRepo ;
        this.accountRepo = accountRepo;
    }
    @Override
    public TransactionResponse createTransaction(UUID id, TransactionRequest transaction) {
        Account account = accountRepo.findById(id).orElse(null);
        if (account == null) {return null;}

        if (transaction.getType().equalsIgnoreCase("CREDIT")) {
            Integer balance = account.getBalance() + transaction.getAmount();
            account.setBalance(balance);
        }
        if (transaction.getType().equalsIgnoreCase("DEBIT")) {
            Integer balance = account.getBalance() - transaction.getAmount();
            account.setBalance(balance);
        }

        accountRepo.save(account);

        Transactions transactions = new Transactions(transaction.getType(),transaction.getAmount(), LocalDateTime.now(), account);
        Transactions saved = transactionsRepo.save(transactions);
        return new TransactionResponse(saved.getId(),saved.getType(),saved.getAccount().getId(),saved.getAccount().getBalance(), saved.getAmount(), saved.getTransactionTime());
    }

    @Override
    public List<TransactionResponse> getTransactions(UUID id) {
        List<Transactions> transactions = transactionsRepo.findByAccountId(id);

        return transactions.stream().map(transaction ->new TransactionResponse(transaction.getId(),transaction.getType(),transaction.getAccount().getId(),transaction.getAccount().getBalance(), transaction.getAmount(), transaction.getTransactionTime())).toList();
    }
}
