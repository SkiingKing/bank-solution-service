package com.bank.task.repository;

import com.bank.task.model.Account;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository {

    Account save(Account account);

    Optional<Account> findById(String id);

    List<Account> findAll();

    void deleteAll();
}
