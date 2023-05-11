package com.sybercenter.core.secority.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class TokenRefreshRequestDTO {
    @NotBlank
    private String refreshToken;
}
