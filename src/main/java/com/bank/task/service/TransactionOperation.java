package com.bank.task.service;

import com.bank.task.domain.DepositRequest;
import com.bank.task.domain.TransferRequest;
import com.bank.task.domain.WithdrawRequest;

public interface TransactionOperation {

    void deposit(DepositRequest depositRequest);

    void withdraw(WithdrawRequest withdrawRequest);

    void transfer(TransferRequest transferRequest);
}
