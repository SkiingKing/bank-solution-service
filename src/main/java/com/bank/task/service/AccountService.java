package com.bank.task.service;

import com.bank.task.domain.CreateAccountRequest;
import com.bank.task.exeption.AccountNotFoundException;
import com.bank.task.mapper.AccountMapper;
import com.bank.task.model.Account;
import com.bank.task.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.bank.task.utils.ErrorMessageUtils.ACCOUNT_NOT_FOUND_ERROR_MESSAGE;

@Service
@RequiredArgsConstructor
@Log4j2
public class AccountService implements AccountOperation {

    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;


    @Override
    public Account addAccount(CreateAccountRequest request) {
        log.info("Creating new account...");

        Account account = accountMapper.toAccount(request);
        account.setAccountId(UUID.randomUUID());

        Account savedAccount = accountRepository.save(account);
        log.info("Account created successfully with ID: {}", savedAccount.getAccountId());
        return savedAccount;
    }

    @Override
    public Account fetchAccountDetails(String accountId) {
        log.info("Fetching account details for id {}", accountId);
        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isEmpty()) {
            throw new AccountNotFoundException(ACCOUNT_NOT_FOUND_ERROR_MESSAGE);
        }
        return account.get();
    }

    @Override
    public List<Account> fetchAllAccounts() {
        log.info("Fetching all accounts...");
        List<Account> accounts = accountRepository.findAll();
        log.info("Found {} accounts.", accounts.size());
        return accounts;
    }
}
