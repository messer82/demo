package com.example.demo.domain.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AccountPatch {

    @NotBlank
    private BigDecimal balance;

    private int accountId;
}
