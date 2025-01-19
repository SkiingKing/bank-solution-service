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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static com.bank.task.TestUtils.createAccount;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    private static final String BASE_URL = "/v1/account";

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
    }

    @Test
    void testCreateAccountWithoutInitialBalance() throws Exception {
        String request = """
                {
                    "name": "TestName",
                    "surname": "TestSurname",
                    "phone": "0672436235"
                }
                """;

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("TestName"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value("TestSurname"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value("0672436235"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value("0"));
    }

    @Test
    void testCreateAccountWithInitialBalance() throws Exception {
        String request = """
                {
                    "name": "TestName",
                    "surname": "TestSurname",
                    "phone": "0672436235",
                    "initialBalance": "100.0"
                }
                """;

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("TestName"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value("TestSurname"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value("0672436235"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value("100.0"));
    }

    @Test
    void testGetAccountDetails() throws Exception {
        Account account = createAccount();
        accountRepository.save(account);

        mockMvc.perform(get(BASE_URL + "/" + account.getAccountId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountId").value(account.getAccountId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(account.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(account.getSurname()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value(account.getPhone()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(account.getBalance()));
    }

    @Test
    void testGetAccountDetailsForNonExistingAccountID() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("Account not found"));
    }

    @Test
    void testGetAccountDetailsForWrongFormatedAccountID() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + "12122-sd-53")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("getAccountDetails.accountId: Not valid format for accountId"));
    }


    @Test
    void testGetAllAccounts() throws Exception {
        Account account1 = createAccount();

        Account account2 = createAccount();
        account2.setAccountId(UUID.randomUUID());

        accountRepository.save(account1);
        accountRepository.save(account2);

        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].accountId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(account1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname").value(account1.getSurname()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].phone").value(account1.getPhone()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].balance").value(account1.getBalance()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].accountId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(account2.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].surname").value(account2.getSurname()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].phone").value(account2.getPhone()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].balance").value(account2.getBalance()));
    }
}
