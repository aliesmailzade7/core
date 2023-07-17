package com.cybercenter.core.dto;

import com.cybercenter.core.constant.ValidationMessages;
import com.cybercenter.core.entity.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@Setter
@ToString
public class UserInfoDTO {

    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String username;

    @NotNull(message = ValidationMessages.USER.FIRST_NAME_REQUIRED)
    private String firstName;

    @NotNull(message = ValidationMessages.USER.LAST_NAME_REQUIRED)
    private String lastName;

    @Pattern(regexp = "1[3-4][0-9]{2}-[0-1][0-9]-[0-3][0-9]", message = ValidationMessages.USER.BIRTH_DAY_WRONG_PATTERN)
    private String birthDay;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String email;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String phoneNumber;

    private String job;

    private Integer education;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String educationTitle;

    private String orientation;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean enable;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Role> roles;
}
