package com.bank.task.service;

import com.bank.task.domain.DepositRequest;
import com.bank.task.domain.TransferRequest;
import com.bank.task.domain.WithdrawRequest;
import com.bank.task.exeption.AccountNotFoundException;
import com.bank.task.model.Account;
import com.bank.task.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.bank.task.utils.ErrorMessageUtils.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class TransactionService implements TransactionOperation {

    private final AccountRepository accountRepository;

    @Override
    public void deposit(DepositRequest depositRequest) {
        Optional<Account> account = accountRepository.findById(depositRequest.getAccountId());
        if (account.isEmpty()) {
            throw new AccountNotFoundException(ACCOUNT_NOT_FOUND_ERROR_MESSAGE);
        }
        account.get().setBalance(account.get().getBalance().add(depositRequest.getAmount()));
    }

    @Override
    public void withdraw(WithdrawRequest withdrawRequest) {
        Optional<Account> account = accountRepository.findById(withdrawRequest.getAccountId());
        if (account.isEmpty()) {
            throw new AccountNotFoundException(ACCOUNT_NOT_FOUND_ERROR_MESSAGE);
        }

        if (account.get().getBalance().compareTo(withdrawRequest.getAmount()) < 0) {
            throw new IllegalArgumentException(INSUFFICIENT_FUNDS_ERROR_MESSAGE);
        }

        account.get().setBalance(account.get().getBalance().subtract(withdrawRequest.getAmount()));
    }

    @Override
    public void transfer(TransferRequest transferRequest) {
        if (transferRequest.getSourceAccountId().equals(transferRequest.getTargetAccountId())) {
            throw new IllegalArgumentException(EQUAL_ACCOUNTS_ID_NOT_SUPPORTED_ERROR_MESSAGE);
        } else {
            Optional<Account> sourceAccount = accountRepository.findById(transferRequest.getSourceAccountId());

            if (sourceAccount.isEmpty()) {
                throw new AccountNotFoundException(SOURCE_ACCOUNT_NOT_FOUND_ERROR_MESSAGE);
            }

            Optional<Account> targetAccount = accountRepository.findById(transferRequest.getTargetAccountId());
            if (targetAccount.isEmpty()) {
                throw new AccountNotFoundException(TARGET_ACCOUNT_NOT_FOUND_ERROR_MESSAGE);
            }

            if (sourceAccount.get().getBalance().compareTo(transferRequest.getAmount()) < 0) {
                throw new IllegalArgumentException(INSUFFICIENT_FUNDS_FOR_SOURCE_ACCOUNT_ERROR_MESSAGE);
            }

            sourceAccount.get().setBalance(sourceAccount.get().getBalance().subtract(transferRequest.getAmount()));
            targetAccount.get().setBalance(targetAccount.get().getBalance().add(transferRequest.getAmount()));
        }
    }
}
