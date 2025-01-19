package com.bank.task.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    private UUID accountId;

    private String name;

    private String surname;

    private String phone;

    private BigDecimal balance;
}
