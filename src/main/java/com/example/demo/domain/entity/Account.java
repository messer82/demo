package com.example.demo.domain.entity;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Account {

    private int account_id;
    private int user_id;
    @NotBlank
    private String accountNumber;
    @NotNull
    private BigDecimal balance;
}
