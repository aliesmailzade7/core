package com.cybercenter.core.secority.constant;

public enum BaseUserRole {
    ROLE_USER("USER"),
    ROLE_AUTHOR( "AUTHOR");

    private String name;

    BaseUserRole(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
