package com.cybercenter.core.secority.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class VerificationDTO implements Serializable {
    private String username;
    private Integer verifyCode;
}
