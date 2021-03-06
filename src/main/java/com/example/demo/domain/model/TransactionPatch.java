package com.example.demo.domain.model;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class TransactionPatch {

    @NotBlank
    @Max(2000)
    @Min(1)
    private BigDecimal amount;

    private int transactionId;
}
