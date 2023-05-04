package com.sybercenter.core.secority.jwt;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class JwtAuth {
    @NotNull(message = "یوزرنیم نمیتواند خالی باشد")
    private String username;
    private String password;
}
