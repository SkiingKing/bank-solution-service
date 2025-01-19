package com.bank.task.repository;

import com.bank.task.model.Account;
import com.bank.task.storage.LocalStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountRepositoryImplTest {


    @Mock
    private LocalStorage localStorage;


    @InjectMocks
    private AccountRepositoryImpl accountRepository;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account(UUID.randomUUID(), "name", "surname", "0673257891", BigDecimal.valueOf(100.05));
    }

    @Test
    void shouldSaveNewAccount() {
        Account savedAccount = accountRepository.save(account);

        assertNotNull(savedAccount);
        assertEquals(account.getAccountId(), savedAccount.getAccountId());
        verify(localStorage, times(1)).save(account);
    }

    @Test
    void shouldFindAccountById() {
        String accountId = account.getAccountId().toString();

        when(localStorage.getAccountById(account.getAccountId())).thenReturn(account);

        Optional<Account> result = accountRepository.findById(accountId);

        assertTrue(result.isPresent());
        assertEquals(account.getAccountId(), result.get().getAccountId());
        verify(localStorage, times(1)).getAccountById(account.getAccountId());
    }

    @Test
    void shouldFindAllAccounts() {
        Account account2 = new Account();
        account2.setAccountId(UUID.randomUUID());
        account2.setBalance(BigDecimal.ONE);

        when(localStorage.getAllAccounts()).thenReturn(Arrays.asList(account, account2));

        List<Account> accounts = accountRepository.findAll();

        assertNotNull(accounts);
        assertEquals(2, accounts.size());
        verify(localStorage, times(1)).getAllAccounts();
    }

}
