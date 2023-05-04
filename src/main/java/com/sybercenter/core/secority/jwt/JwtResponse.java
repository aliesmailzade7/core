package com.sybercenter.core.secority.jwt;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
public class JwtResponse {
    private String access_token;
    private String token_type;
    private Long expires_in;
    private Set<String> scope;
}
