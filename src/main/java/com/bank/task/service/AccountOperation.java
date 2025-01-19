package com.bank.task.service;

import com.bank.task.domain.CreateAccountRequest;
import com.bank.task.model.Account;

import java.util.List;

public interface AccountOperation {

    Account addAccount(CreateAccountRequest request);

    Account fetchAccountDetails(String accountId);

    List<Account> fetchAllAccounts();
}
