package com.sybercenter.core.secority.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class JwtResponseDTO {
    private String accessToken;
    private String tokenType;
    private Long expiresIn;
    private Set<String> scope;
}
