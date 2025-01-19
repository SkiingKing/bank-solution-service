package com.bank.task.service;

import com.bank.task.domain.DepositRequest;
import com.bank.task.domain.TransferRequest;
import com.bank.task.domain.WithdrawRequest;
import com.bank.task.exeption.AccountNotFoundException;
import com.bank.task.model.Account;
import com.bank.task.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static com.bank.task.TestUtils.createAccount;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionService transactionService;


    @Test
    void testDepositSuccess() {
        Account account = createAccount();
        String accountId = account.getAccountId().toString();
        DepositRequest depositRequest = new DepositRequest();
        depositRequest.setAccountId(accountId);
        depositRequest.setAmount(BigDecimal.ONE);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        BigDecimal previousBalance = account.getBalance();
        transactionService.deposit(depositRequest);

        assertEquals(depositRequest.getAmount().add(previousBalance), account.getBalance());
        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    void testDepositAccountNotFound() {
        String accountId = UUID.randomUUID().toString();
        DepositRequest depositRequest = new DepositRequest();
        depositRequest.setAccountId(accountId);

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class,
                () -> transactionService.deposit(depositRequest));

        assertEquals("Account not found", exception.getMessage());
        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    void testWithdrawSuccess() {
        Account account = createAccount();
        String accountId = account.getAccountId().toString();
        WithdrawRequest withdrawRequest = new WithdrawRequest();
        withdrawRequest.setAccountId(accountId);
        withdrawRequest.setAmount(BigDecimal.ONE);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        BigDecimal previousBalance = account.getBalance();
        transactionService.withdraw(withdrawRequest);

        assertEquals(previousBalance.subtract(withdrawRequest.getAmount()), account.getBalance());
        verify(accountRepository, times(1)).findById(anyString());
    }

    @Test
    void testWithdrawWithInsufficientFunds() {
        Account account = createAccount();
        WithdrawRequest withdrawRequest = new WithdrawRequest();
        withdrawRequest.setAccountId(account.getAccountId().toString());
        withdrawRequest.setAmount(BigDecimal.valueOf(1500));

        when(accountRepository.findById(account.getAccountId().toString())).thenReturn(Optional.of(account));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transactionService.withdraw(withdrawRequest));

        assertEquals("Insufficient funds", exception.getMessage());
        verify(accountRepository, times(1)).findById(anyString());
    }

    @Test
    void testTransferSuccess() {
        Account sourceAccount = createAccount();
        Account targetAccount = new Account();
        targetAccount.setAccountId(UUID.randomUUID());
        targetAccount.setBalance(BigDecimal.valueOf(500));

        String sourceAccountId = sourceAccount.getAccountId().toString();
        String targetAccountId = targetAccount.getAccountId().toString();

        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setSourceAccountId(sourceAccountId);
        transferRequest.setTargetAccountId(targetAccountId);
        transferRequest.setAmount(BigDecimal.valueOf(50));

        when(accountRepository.findById(sourceAccountId)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findById(targetAccountId)).thenReturn(Optional.of(targetAccount));

        transactionService.transfer(transferRequest);

        assertEquals(BigDecimal.valueOf(50.05), sourceAccount.getBalance());
        assertEquals(BigDecimal.valueOf(550), targetAccount.getBalance());
        verify(accountRepository).findById(sourceAccountId);
        verify(accountRepository).findById(targetAccountId);
    }

    @Test
    void testTransferWithEqualAccountIds() {
        String accountId = UUID.randomUUID().toString();
        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setSourceAccountId(accountId);
        transferRequest.setTargetAccountId(accountId);
        transferRequest.setAmount(BigDecimal.ONE);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transactionService.transfer(transferRequest));

        assertEquals("Equal account ids not supported", exception.getMessage());
        verify(accountRepository, times(0)).findById(transferRequest.getSourceAccountId());
        verify(accountRepository, times(0)).findById(transferRequest.getTargetAccountId());
    }

    @Test
    void testTransferSourceAccountNotFound() {
        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setSourceAccountId(UUID.randomUUID().toString());
        transferRequest.setTargetAccountId(UUID.randomUUID().toString());
        transferRequest.setAmount(BigDecimal.ONE);

        when(accountRepository.findById(transferRequest.getSourceAccountId())).thenReturn(Optional.empty());

        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class,
                () -> transactionService.transfer(transferRequest));

        assertEquals("Source account not found", exception.getMessage());
        verify(accountRepository, times(1)).findById(transferRequest.getSourceAccountId());
    }

    @Test
    void testTransferTargetAccountNotFound() {
        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setSourceAccountId(UUID.randomUUID().toString());
        transferRequest.setTargetAccountId(UUID.randomUUID().toString());
        transferRequest.setAmount(BigDecimal.ONE);

        when(accountRepository.findById(transferRequest.getSourceAccountId())).thenReturn(Optional.of(new Account()));
        when(accountRepository.findById(transferRequest.getTargetAccountId())).thenReturn(Optional.empty());

        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class,
                () -> transactionService.transfer(transferRequest));

        assertEquals("Target account not found", exception.getMessage());
        verify(accountRepository, times(1)).findById(transferRequest.getSourceAccountId());
        verify(accountRepository, times(1)).findById(transferRequest.getTargetAccountId());
    }

    @Test
    void testTransferWithInsufficientFunds() {
        Account sourceAccount = createAccount();
        Account targetAccount = new Account();
        targetAccount.setAccountId(UUID.randomUUID());
        targetAccount.setBalance(BigDecimal.valueOf(500));

        String sourceAccountId = sourceAccount.getAccountId().toString();
        String targetAccountId = targetAccount.getAccountId().toString();

        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setSourceAccountId(sourceAccountId);
        transferRequest.setTargetAccountId(targetAccountId);
        transferRequest.setAmount(BigDecimal.valueOf(200));

        when(accountRepository.findById(sourceAccountId)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findById(targetAccountId)).thenReturn(Optional.of(targetAccount));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transactionService.transfer(transferRequest));

        assertEquals("Insufficient funds in source account", exception.getMessage());
        verify(accountRepository, times(1)).findById(transferRequest.getSourceAccountId());
        verify(accountRepository, times(1)).findById(transferRequest.getTargetAccountId());
    }
}
