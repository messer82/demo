package com.example.demo.domain.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AccountPatch {

    @NotBlank
    private double balance;

    private int accountId;
}
