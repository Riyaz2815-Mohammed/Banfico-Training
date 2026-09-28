package com.riyaz.banficotrainingprogram.transaction.service.impl;

import com.riyaz.banficotrainingprogram.account.entity.Account;
import com.riyaz.banficotrainingprogram.account.repository.AccountRepo;
import com.riyaz.banficotrainingprogram.customer.entity.Customer;
import com.riyaz.banficotrainingprogram.customer.repository.CustomerRepo;
import com.riyaz.banficotrainingprogram.exception.InsufficientBalanceException;
import com.riyaz.banficotrainingprogram.exception.ResourceNotFoundException;
import com.riyaz.banficotrainingprogram.transaction.dto.TransferRequest;
import com.riyaz.banficotrainingprogram.transaction.dto.TransferResponse;
import com.riyaz.banficotrainingprogram.transaction.entity.Transactions;
import com.riyaz.banficotrainingprogram.transaction.repository.TransactionsRepo;
import com.riyaz.banficotrainingprogram.transaction.service.TransferService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransferServiceImpl implements TransferService {
    private final CustomerRepo customerRepo;
    private final AccountRepo accountRepo;
    private final TransactionsRepo transactionsRepo;

    public TransferServiceImpl(CustomerRepo customerRepo, AccountRepo accountRepo, TransactionsRepo transactionsRepo) {
        this.customerRepo = customerRepo;
        this.accountRepo = accountRepo;
        this.transactionsRepo = transactionsRepo;
    }

    @Override
    @Transactional
    public TransferResponse transfer(String email, TransferRequest request) {
        Customer sender = customerRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("No customer record found for email: " + email));
        Account fromAccount = accountRepo.findById(request.getFromAccountId()).orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + request.getFromAccountId()));
        if (!fromAccount.getCustomer().getId().equals(sender.getId())) throw new ResourceNotFoundException("Account not found with id: " + request.getFromAccountId());
        Account toAccount = accountRepo.findByAccountNo(request.getRecipientAccountNo()).orElseThrow(() -> new ResourceNotFoundException("No account found with number: " + request.getRecipientAccountNo()));
        if (fromAccount.getId().equals(toAccount.getId())) throw new InsufficientBalanceException("Cannot transfer to the same account");
        if (fromAccount.getBalance() < request.getAmount()) throw new InsufficientBalanceException("Insufficient balance: available " + fromAccount.getBalance() + ", requested " + request.getAmount());
        fromAccount.setBalance(fromAccount.getBalance() - request.getAmount());
        accountRepo.save(fromAccount);
        toAccount.setBalance(toAccount.getBalance() + request.getAmount());
        accountRepo.save(toAccount);
        String recipientName = toAccount.getCustomer().getFirstName() + " " + toAccount.getCustomer().getLastName();
        LocalDateTime now = LocalDateTime.now();
        Transactions debitTx = transactionsRepo.save(new Transactions("DEBIT", request.getAmount(), now, fromAccount, fromAccount.getBalance()));
        transactionsRepo.save(new Transactions("CREDIT", request.getAmount(), now, toAccount, toAccount.getBalance()));
        String note = request.getNote() != null ? request.getNote() : "";
        return new TransferResponse(debitTx.getId(), fromAccount.getAccountNo(), fromAccount.getBalance(), recipientName, toAccount.getAccountNo(), request.getAmount(), note, now);
    }
}
