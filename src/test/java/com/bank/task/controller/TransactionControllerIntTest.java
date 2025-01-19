package com.bank.task.controller;

import com.bank.task.model.Account;
import com.bank.task.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    private static final String BASE_URL = "/v1/transaction";

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
    }

    @Test
    void testDeposit() throws Exception {
        Account account = createAccount();
        String accountId = account.getAccountId().toString();

        mockMvc.perform(post(BASE_URL + "/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "accountId": "%s",
                                    "amount": 100.0
                                }
                                """.formatted(accountId)))
                .andExpect(status().isOk())
                .andExpect(content().string("Deposit successful"));

        Optional<Account> updatedAccount = accountRepository.findById(accountId);
        assertThat(updatedAccount.get().getBalance().compareTo(BigDecimal.valueOf(200.05))).isEqualTo(0);
    }

    @Test
    void testWithdraw() throws Exception {
        Account account = createAccount();
        String accountId = account.getAccountId().toString();

        mockMvc.perform(post(BASE_URL + "/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "accountId": "%s",
                                    "amount": 100.0
                                }
                                """.formatted(accountId)))
                .andExpect(status().isOk())
                .andExpect(content().string("Withdrawal successful"));

        Optional<Account> updatedAccount = accountRepository.findById(accountId);
        assertThat(updatedAccount.get().getBalance().compareTo(BigDecimal.valueOf(0.05))).isEqualTo(0);
    }

    @Test
    void testWithdrawWithInsufficientFunds() throws Exception {
        Account account = createAccount();
        String accountId = account.getAccountId().toString();

        mockMvc.perform(post(BASE_URL + "/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "accountId": "%s",
                                    "amount": 200.0
                                }
                                """.formatted(accountId)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Insufficient funds"));
    }

    @Test
    void testTransfer() throws Exception {
        Account sourceAccount = createAccount();
        Account targetAccount = createAccount();

        mockMvc.perform(post(BASE_URL + "/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "sourceAccountId": "%s",
                                    "targetAccountId": "%s",
                                    "amount": 50.0
                                }
                                """.formatted(sourceAccount.getAccountId(), targetAccount.getAccountId())))
                .andExpect(status().isOk())
                .andExpect(content().string("Transfer successful"));

        Optional<Account> updatedSourceAccount = accountRepository.findById(sourceAccount.getAccountId().toString());
        Optional<Account> updatedTargetAccount = accountRepository.findById(targetAccount.getAccountId().toString());

        assertThat(updatedSourceAccount.get().getBalance().compareTo(new BigDecimal("50.05"))).isEqualTo(0);
        assertThat(updatedTargetAccount.get().getBalance().compareTo(new BigDecimal("150.05"))).isEqualTo(0);
    }

    @Test
    void testTransferWithInsufficientFunds() throws Exception {
        Account sourceAccount = createAccount();
        Account targetAccount = createAccount();

        mockMvc.perform(post(BASE_URL + "/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "sourceAccountId": "%s",
                                    "targetAccountId": "%s",
                                    "amount": 200.0
                                }
                                """.formatted(sourceAccount.getAccountId(), targetAccount.getAccountId())))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Insufficient funds in source account"));
    }

    private Account createAccount() {
        Account account = new Account();
        account.setAccountId(UUID.randomUUID());
        account.setName("TestName");
        account.setSurname("TestSurname");
        account.setPhone("0672436235");
        account.setBalance(BigDecimal.valueOf(100.05));
        return accountRepository.save(account);
    }
}
