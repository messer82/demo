package com.example.demo.domain.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserPatch {

    @NotBlank
    private String userName;

    @NotBlank
    @Email
    private String email;

    private int userId;
}
