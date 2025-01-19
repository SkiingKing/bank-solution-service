package com.bank.task.mapper;

import com.bank.task.domain.CreateAccountRequest;
import com.bank.task.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.math.BigDecimal;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountMapper {

    @Mapping(target = "accountId", ignore = true)
    @Mapping(target = "balance", expression = "java(mapInitialBalance(accountRequest.getInitialBalance()))")
    Account toAccount(CreateAccountRequest accountRequest);

    default BigDecimal mapInitialBalance(BigDecimal initialBalance) {
        return initialBalance != null ? initialBalance : BigDecimal.ZERO;
    }
}
