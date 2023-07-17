package com.cybercenter.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserExistDTO {
    private boolean hasAccount;
    private String message;
    private String loginMethodType;
    private Boolean isEnable;
}
