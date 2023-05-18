package com.cybercenter.core.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserExistDTO {
    private boolean hasAccount;
    private String loginMethodType;
    private Boolean isEnable;
}
