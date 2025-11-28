package com.example.chart_backend.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RegisterRequest {

    @JsonAlias({"userFullname", "fullName"})
    private String userFullname;

    @JsonAlias({"userPassword", "password"})
    private String userPassword;

    private String userPhone;

    private String bhxhNumber;
    private String citizenId;
    private LocalDate dateOfBirth;
    private String address;

    @JsonAlias({"cardNumber", "card"})
    private String cardNumber;

    private LocalDate cardIssuedDate;
    private LocalDate cardExpiryDate;
    private String hospitalRegistered;
    private String cardStatus;

    private String avatarUrl;
}

