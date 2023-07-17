package com.cybercenter.core.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class TokenRefreshRequestDTO {
    @NotBlank
    private String refreshToken;
}
