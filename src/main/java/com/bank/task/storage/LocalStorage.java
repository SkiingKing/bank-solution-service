package com.bank.task.storage;

import com.bank.task.model.Account;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LocalStorage {

    private final Map<UUID, Account> accountsMap = new ConcurrentHashMap<>();

    public void save(Account account) {
        accountsMap.putIfAbsent(account.getAccountId(), account);
    }

    public Account getAccountById(UUID accountId) {
        return accountsMap.get(accountId);
    }

    public List<Account> getAllAccounts() {
        return new ArrayList<>(accountsMap.values());
    }

    public void deleteAll() {
        accountsMap.clear();
    }


}
