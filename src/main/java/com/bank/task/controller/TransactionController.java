package com.bank.task.controller;

import com.bank.task.domain.DepositRequest;
import com.bank.task.domain.TransferRequest;
import com.bank.task.domain.WithdrawRequest;
import com.bank.task.exeption.ErrorResponse;
import com.bank.task.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/transaction")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})
    })
    @Operation(summary = "Deposit",
            description = "Deposit funds into an account. A successful request returns a '200 OK' status."
    )
    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@Valid @RequestBody DepositRequest depositRequest) {
        transactionService.deposit(depositRequest);
        return ResponseEntity.ok("Deposit successful");
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})
    })
    @Operation(summary = "Withdraw",
            description = "Withdraw funds from an account. A successful request returns a '200 OK' status."
    )
    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@Valid @RequestBody WithdrawRequest withdrawRequest) {
        transactionService.withdraw(withdrawRequest);
        return ResponseEntity.ok("Withdrawal successful");
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})
    })
    @Operation(summary = "Transfer",
            description = "Transfer funds between two accounts. A successful request returns a '200 OK' status."
    )
    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@Valid @RequestBody TransferRequest transferRequest) {
        transactionService.transfer(transferRequest);
        return ResponseEntity.ok("Transfer successful");
    }

}
