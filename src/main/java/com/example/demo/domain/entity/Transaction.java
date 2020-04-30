package com.example.demo.domain.entity;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Transaction {

    private int transaction_id;
    private int account_id;
    @NotBlank
    private String destination_account;
    @NotNull
    @Max(2000)
    @Min(1)
    private BigDecimal amount_paid;
    private LocalDate transaction_date;
}
