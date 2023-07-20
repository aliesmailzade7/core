package com.cybercenter.core.dto;

import com.cybercenter.core.constant.ValidationMessages;
import com.cybercenter.core.entity.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;
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

    @Email(message = ValidationMessages.USER.EMAIL_ADDRESS_WRONG_PATTERN)
    private String email;

    @Max(value = 999999, message = ValidationMessages.AUTH.VERIFY_CODE_INVALID)
    @Min(value = 0, message = ValidationMessages.AUTH.VERIFY_CODE_INVALID)
    private Integer verifyEmailCode;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isVerifyEmail;

    @Pattern(regexp = "^(09[0-9]{9})$", message = ValidationMessages.USER.PHONE_NUMBER_WRONG_PATTERN)
    private String phoneNumber;

    @Max(value = 999999, message = ValidationMessages.AUTH.VERIFY_CODE_INVALID)
    @Min(value = 0, message = ValidationMessages.AUTH.VERIFY_CODE_INVALID)
    private Integer verifyPhoneNumberCode;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isVerifyPhoneNumber;

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
