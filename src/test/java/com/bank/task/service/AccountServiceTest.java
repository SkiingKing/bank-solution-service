package com.bank.task.service;

import com.bank.task.domain.CreateAccountRequest;
import com.bank.task.exeption.AccountNotFoundException;
import com.bank.task.mapper.AccountMapper;
import com.bank.task.model.Account;
import com.bank.task.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.bank.task.TestUtils.createAccount;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;


    @Test
    void testAddNewAccount() {
        CreateAccountRequest accountRequest = new CreateAccountRequest();
        Account account = createAccount();

        when(accountMapper.toAccount(accountRequest)).thenReturn(account);
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Account savedAccount = accountService.addAccount(accountRequest);

        assertNotNull(savedAccount);
        assertNotNull(savedAccount.getAccountId());
        verify(accountMapper, times(1)).toAccount(accountRequest);
        verify(accountRepository, times(1)).save(savedAccount);
    }

    @Test
    void testFetchAccountDetails() {
        String accountId = UUID.randomUUID().toString();
        Account account = createAccount();
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        Account fetchedAccount = accountService.fetchAccountDetails(accountId);

        assertNotNull(fetchedAccount);
        assertEquals(account.getAccountId(), fetchedAccount.getAccountId());
        assertEquals(account.getName(), fetchedAccount.getName());
        assertEquals(account.getSurname(), fetchedAccount.getSurname());
        assertEquals(account.getPhone(), fetchedAccount.getPhone());
        assertEquals(account.getBalance(), fetchedAccount.getBalance());
        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    void testFetchAccountDetailsForNonExistingAccount() {
        String accountId = UUID.randomUUID().toString();
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class,
                () -> accountService.fetchAccountDetails(accountId));

        assertEquals("Account not found", exception.getMessage());
        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    void testFetchAllAccounts() {
        Account account = createAccount();
        when(accountRepository.findAll()).thenReturn(Collections.singletonList(account));

        List<Account> accounts = accountService.fetchAllAccounts();

        assertNotNull(accounts);
        assertEquals(1, accounts.size());
        assertEquals(account.getAccountId(), accounts.get(0).getAccountId());
        verify(accountRepository, times(1)).findAll();
    }

}
