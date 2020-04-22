package com.example.demo.domain.entity;

import lombok.*;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;

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
    @NotBlank
    private double balance;
}
