package com.sybercenter.core.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuth {
    private String username;
    private String password;
}
