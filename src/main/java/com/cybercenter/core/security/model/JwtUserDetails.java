package com.cybercenter.core.security.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtUserDetails {
    private  String id;
    private  String username;
    private  String firstName;
    private  String lastName;
    private  String phoneNumber;
    private  List<String> roles;
    private  List<String> authorities;
}
