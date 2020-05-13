package com.example.demo.domain.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
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
