package com.unforgettable.securitypart.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RegistrationRequest {
    private String username;
    private String password;
    @JsonProperty(value = "confirmed_password")
    private String confirmedPassword;
}
