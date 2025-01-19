package com.bank.task.repository;

import com.bank.task.model.Account;
import com.bank.task.storage.LocalStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {

    private final LocalStorage localStorage;

    @Override
    public Account save(Account account) {
        localStorage.save(account);
        return account;
    }

    @Override
    public Optional<Account> findById(String id) {
        return Optional.ofNullable(localStorage.getAccountById(UUID.fromString(id)));
    }

    @Override
    public List<Account> findAll() {
        return localStorage.getAllAccounts();
    }

    @Override
    public void deleteAll() {
        localStorage.deleteAll();
    }
}
