package com.sybercenter.core.secority.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserExistDTO {
    private boolean hasAccount;
    private String loginMethodType;
    private Boolean isEnable;
}
