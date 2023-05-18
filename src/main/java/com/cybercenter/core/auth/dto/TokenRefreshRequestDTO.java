package com.cybercenter.core.auth.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class TokenRefreshRequestDTO {
    @NotBlank
    private String refreshToken;
}
