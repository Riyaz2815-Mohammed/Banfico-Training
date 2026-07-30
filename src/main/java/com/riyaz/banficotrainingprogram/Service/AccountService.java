package com.riyaz.banficotrainingprogram.Service;

import com.riyaz.banficotrainingprogram.dto.AccountRequest;
import com.riyaz.banficotrainingprogram.dto.AccountResponse;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    AccountResponse createAccount(AccountRequest accountRequest);
    List<AccountResponse> getAccounts();
    AccountResponse getAccount(UUID accountId);
    AccountResponse updateAccount(UUID id,AccountRequest accountRequest);
    void deleteAccount(UUID accountId);
}
