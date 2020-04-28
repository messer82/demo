package com.example.demo.domain.entity;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class User {

    private Integer user_id;
    @NotBlank
    private String userName;
    @NotBlank
    @Email(message = "Invalid email address!")
    private String email;
    @NotNull
    private LocalDate birthDate;
}
