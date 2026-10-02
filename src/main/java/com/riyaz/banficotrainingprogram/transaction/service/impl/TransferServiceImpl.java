package com.riyaz.banficotrainingprogram.transaction.service.impl;

import com.riyaz.banficotrainingprogram.account.entity.Account;
import com.riyaz.banficotrainingprogram.account.repository.AccountRepo;
import com.riyaz.banficotrainingprogram.customer.entity.Customer;
import com.riyaz.banficotrainingprogram.customer.repository.CustomerRepo;
import com.riyaz.banficotrainingprogram.exception.InsufficientBalanceException;
import com.riyaz.banficotrainingprogram.exception.ResourceNotFoundException;
import com.riyaz.banficotrainingprogram.payment.dto.PaymentResponse;
import com.riyaz.banficotrainingprogram.payment.entity.Payment;
import com.riyaz.banficotrainingprogram.payment.entity.PaymentStatus;
import com.riyaz.banficotrainingprogram.payment.repository.PaymentRepo;
import com.riyaz.banficotrainingprogram.transaction.dto.TransferPreviewResponse;
import com.riyaz.banficotrainingprogram.transaction.dto.TransferRequest;
import com.riyaz.banficotrainingprogram.transaction.entity.Transactions;
import com.riyaz.banficotrainingprogram.transaction.repository.TransactionsRepo;
import com.riyaz.banficotrainingprogram.transaction.service.TransferService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransferServiceImpl implements TransferService {
    private final CustomerRepo customerRepo;
    private final AccountRepo accountRepo;
    private final TransactionsRepo transactionsRepo;
    private final PaymentRepo paymentRepo;

    public TransferServiceImpl(CustomerRepo customerRepo, AccountRepo accountRepo, TransactionsRepo transactionsRepo, PaymentRepo paymentRepo) {
        this.customerRepo = customerRepo;
        this.accountRepo = accountRepo;
        this.transactionsRepo = transactionsRepo;
        this.paymentRepo = paymentRepo;
    }

    private PaymentResponse toPaymentResponse(Payment p) {
        String name = p.getInitiatedBy().getFirstName() + " " + p.getInitiatedBy().getLastName();
        return new PaymentResponse(p.getPaymentId(), p.getFromAccount().getAccountNo(), p.getToAccountNo(), p.getToAccountName(), p.getAmount(), p.getNote(), p.getStatus(), name, p.getInitiatedAt(), p.getCompletedAt(), p.getFailureReason());
    }

    @Override
    public TransferPreviewResponse preview(String email, UUID fromAccountId, String recipientAccountNo, Integer amount) {
        Customer sender = customerRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("No customer record found for email: " + email));
        Account fromAccount = accountRepo.findById(fromAccountId).orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + fromAccountId));
        if (!fromAccount.getCustomer().getId().equals(sender.getId())) throw new ResourceNotFoundException("Account not found with id: " + fromAccountId);
        Account toAccount = accountRepo.findByAccountNo(recipientAccountNo).orElseThrow(() -> new ResourceNotFoundException("No account found with number: " + recipientAccountNo));
        if (fromAccount.getId().equals(toAccount.getId())) throw new InsufficientBalanceException("Cannot transfer to the same account");
        return new TransferPreviewResponse(fromAccount.getAccountNo(), fromAccount.getBalance(), toAccount.getAccountNo(), toAccount.getCustomer().getFirstName() + " " + toAccount.getCustomer().getLastName(), amount, LocalDateTime.now());
    }

    @Override
    @Transactional
    public PaymentResponse transfer(String email, TransferRequest request) {
        Optional<Payment> existing = paymentRepo.findById(request.getPaymentId());
        if (existing.isPresent() && existing.get().getStatus() == PaymentStatus.COMPLETED) return toPaymentResponse(existing.get());
        if (existing.isPresent() && existing.get().getStatus() == PaymentStatus.PENDING) throw new ResponseStatusException(HttpStatus.CONFLICT, "Payment is already in progress");

        Customer sender = customerRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("No customer record found for email: " + email));
        Account fromAccount = accountRepo.findById(request.getFromAccountId()).orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + request.getFromAccountId()));
        if (!fromAccount.getCustomer().getId().equals(sender.getId())) throw new ResourceNotFoundException("Account not found with id: " + request.getFromAccountId());
        Account toAccount = accountRepo.findByAccountNo(request.getRecipientAccountNo()).orElseThrow(() -> new ResourceNotFoundException("No account found with number: " + request.getRecipientAccountNo()));
        if (fromAccount.getId().equals(toAccount.getId())) throw new InsufficientBalanceException("Cannot transfer to the same account");

        String recipientName = toAccount.getCustomer().getFirstName() + " " + toAccount.getCustomer().getLastName();
        Payment payment = paymentRepo.save(new Payment(request.getPaymentId(), fromAccount, request.getRecipientAccountNo(), recipientName, request.getAmount(), request.getNote(), sender));

        try {
            if (fromAccount.getBalance() < request.getAmount()) throw new InsufficientBalanceException("Insufficient balance: available " + fromAccount.getBalance() + ", requested " + request.getAmount());
            fromAccount.setBalance(fromAccount.getBalance() - request.getAmount());
            accountRepo.save(fromAccount);
            toAccount.setBalance(toAccount.getBalance() + request.getAmount());
            accountRepo.save(toAccount);
            LocalDateTime now = LocalDateTime.now();
            String note = request.getNote() != null && !request.getNote().isBlank() ? " - " + request.getNote() : "";
            transactionsRepo.save(new Transactions("DEBIT", request.getAmount(), now, fromAccount, fromAccount.getBalance(), "Transfer to " + recipientName + note));
            transactionsRepo.save(new Transactions("CREDIT", request.getAmount(), now, toAccount, toAccount.getBalance(), "Transfer from " + (sender.getFirstName() + " " + sender.getLastName()) + note));
            payment.setStatus(PaymentStatus.COMPLETED);
            payment.setCompletedAt(now);
        } catch (Exception e) {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailureReason(e.getMessage());
            paymentRepo.save(payment);
            throw e;
        }

        return toPaymentResponse(paymentRepo.save(payment));
    }
}
