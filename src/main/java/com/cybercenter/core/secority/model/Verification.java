package com.cybercenter.core.secority.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class Verification implements Serializable {
    private String username;
    private Integer verifyCode;
}
