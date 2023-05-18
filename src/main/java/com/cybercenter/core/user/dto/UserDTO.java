package com.cybercenter.core.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.cybercenter.core.user.entity.Role;
import com.cybercenter.core.base.constant.ValidationMessages;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
@ToString
public class UserDTO {
    private Long id;
    @NotNull(message = ValidationMessages.USER.USERNAME_REQUIRED)
    private String username;

    @NotNull(message = ValidationMessages.USER.PASSWORD_REQUIRED)
    @Size(min = 4, message = ValidationMessages.USER.PASSWORD_LENGTH_VALIDATION)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull(message = ValidationMessages.USER.FIRST_NAME_REQUIRED)
    private String firstName;

    @NotNull(message = ValidationMessages.USER.LAST_NAME_REQUIRED)
    private String lastName;

    @Email(message = ValidationMessages.USER.EMAIL_ADDRESS_WRONG_PATTERN)
    private String email;

    @NotNull(message = ValidationMessages.USER.PHONE_NUMBER_REQUIRED)
    @Pattern(regexp = "^(09[0-9]{9})$", message = ValidationMessages.USER.PHONE_NUMBER_WRONG_PATTERN)
    private String phoneNumber;

    @NotNull(message = ValidationMessages.USER.VERIFY_CODE_REQUIRED)
    @Max(value = 999999, message = ValidationMessages.AUTH.VERIFY_CODE_INVALID)
    @Min(value = 0, message = ValidationMessages.AUTH.VERIFY_CODE_INVALID)
    private Integer verifyCode;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean enable = true;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String loginMethodType;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Role> roles;

}
