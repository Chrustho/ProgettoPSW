package com.example.progettopsw.support;

import com.example.progettopsw.entities.Users;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
@Setter
@Getter
@Data
public class UserRegistrationRequest {
    // Getters and Setters
    @Valid
    private Users user;

    @NotBlank(message = "Password obbligatoria")
    private String password;

    public UserRegistrationRequest(Users user, String password) {
        this.user = user;
        this.password = password;
    }
}
