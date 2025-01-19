package com.bank.task;

import com.bank.task.model.Account;

import java.math.BigDecimal;
import java.util.UUID;

public class TestUtils {

    public static Account createAccount() {
        return new Account(UUID.randomUUID(), "name",
                "surname", "0673257891", BigDecimal.valueOf(100.05));
    }
}
