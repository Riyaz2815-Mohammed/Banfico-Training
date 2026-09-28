package com.riyaz.banficotrainingprogram.account.service;

import com.riyaz.banficotrainingprogram.account.dto.AccountLookupResponse;
import com.riyaz.banficotrainingprogram.account.dto.AccountRequest;
import com.riyaz.banficotrainingprogram.account.dto.AccountResponse;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    AccountResponse createAccount(AccountRequest accountRequest);
    List<AccountResponse> getAccounts();
    List<AccountResponse> getMyAccounts(String email);
    AccountResponse getAccount(UUID accountId);
    AccountResponse updateAccount(UUID id, AccountRequest accountRequest);
    void deleteAccount(UUID accountId);
    AccountLookupResponse lookupByAccountNo(String accountNo);
}
