package com.bank.task.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountRequest {

    @Schema(
            implementation = String.class,
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "Vova",
            description = "Name")
    @NotNull
    private String name;

    @Schema(
            implementation = String.class,
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "Shvets",
            description = "Surname")
    @NotNull
    private String surname;

    @Schema(
            implementation = String.class,
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "0674329054",
            description = "Phone number")
    @NotNull
    @Pattern(regexp = "^\\+?[0-9]{10,15}$",
            message = "Invalid phone number")
    private String phone;

    @Schema(
            implementation = String.class,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            example = "100",
            description = "Initial balance amount. Must be a positive number.")
    @PositiveOrZero
    private BigDecimal initialBalance;
}
