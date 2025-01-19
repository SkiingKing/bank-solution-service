package com.bank.task.mapper;

import com.bank.task.domain.CreateAccountRequest;
import com.bank.task.model.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class AccountMapperTest {

    @Autowired
    private AccountMapper accountMapper;

    @Test
    void shouldMapCreateAccountRequestWithoutInitialBalanceToAccount() {
        CreateAccountRequest request = new CreateAccountRequest("name", "surname", "0635526732", null);

        Account account = accountMapper.toAccount(request);

        assertNotNull(account);
        assertEquals(request.getName(), account.getName());
        assertEquals(request.getSurname(), account.getSurname());
        assertEquals(request.getPhone(), account.getPhone());

        assertNotNull(account.getBalance());
        assertEquals(BigDecimal.ZERO, account.getBalance());
    }

    @Test
    void shouldMapCreateAccountRequestWithInitialBalanceToAccount() {
        CreateAccountRequest request = new CreateAccountRequest("name", "surname", "0635526732", BigDecimal.ONE);

        Account account = accountMapper.toAccount(request);

        assertNotNull(account);
        assertEquals(request.getName(), account.getName());
        assertEquals(request.getSurname(), account.getSurname());
        assertEquals(request.getPhone(), account.getPhone());
        assertEquals(request.getInitialBalance(), account.getBalance());
    }

}
