package com.management.transaction.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDTO {
    @NotBlank(message = "Proper error code is required")
    private String errorCode;

    @NotBlank(message = "Descriptive error message is required")
    private String errorMessage;

    private LocalDateTime timeStamp;
}
