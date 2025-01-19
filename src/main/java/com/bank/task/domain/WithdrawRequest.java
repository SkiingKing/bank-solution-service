package com.bank.task.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WithdrawRequest {

    @Schema(
            implementation = String.class,
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "0cf24be8-5572-4648-ae8e-4ae11cd46bf7",
            description = "Account id")
    @Pattern(regexp = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}",
            message = "Not valid format for accountId")
    @NotNull
    private String accountId;

    @Schema(
            implementation = BigDecimal.class,
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "50",
            description = "Withdraw amount. Must be a positive number.")
    @NotNull
    @Positive
    private BigDecimal amount;
}
