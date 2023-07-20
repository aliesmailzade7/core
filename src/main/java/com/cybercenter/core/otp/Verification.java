package com.cybercenter.core.otp;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class Verification implements Serializable {
    private String key;
    private Integer verifyCode;
}
