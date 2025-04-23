package com.management.transaction.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    @NotBlank(message = "Account holder name is required")
    private String fromAccount;

    @NotBlank(message = "Account holder name is required")
    private String toAccount;

    @PositiveOrZero(message = "Initial balance must be non-negative")
    private BigDecimal amount;
}
