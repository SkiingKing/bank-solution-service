package com.bank.task.controller;

import com.bank.task.domain.CreateAccountRequest;
import com.bank.task.exeption.ErrorResponse;
import com.bank.task.model.Account;
import com.bank.task.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/account")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AccountController {

    private final AccountService accountService;

    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Account.class))}),
            @ApiResponse(responseCode = "400", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})
    })
    @Operation(summary = "Create new account",
            description = "Create new account. A successful request returns a '201 Created' status."
    )
    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.addAccount(request));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            type = "object",
                            implementation = Account.class)
            )),
            @ApiResponse(responseCode = "400", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})
    })
    @Operation(
            summary = "Get account details",
            description = "Get account details by account id"
    )
    @GetMapping("/{accountId}")
    public ResponseEntity<Account> getAccountDetails(@PathVariable
                                                     @Pattern(regexp = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}",
                                                             message = "Not valid format for accountId")
                                                     String accountId) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.fetchAccountDetails(accountId));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            type = "array",
                            implementation = Account.class)
            )),
            @ApiResponse(responseCode = "400", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})
    })
    @Operation(
            summary = "Get all accounts",
            description = "Get all accounts"
    )
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.fetchAllAccounts());
    }
}
