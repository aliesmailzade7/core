package com.cybercenter.core.constant;

public enum BaseUserRole {
    ROLE_ADMIN("ADMIN", "ادمین"),
    ROLE_USER("USER", "کاربر عادی"),
    ROLE_AUTHOR_3( "AUTHOR_3", "نویسنده سطح 3"),
    ROLE_AUTHOR_2( "AUTHOR_2", "نویسنده سطح 2"),
    ROLE_AUTHOR_1( "AUTHOR_1", "نویسنده سطح 1"),
    ROLE_ARBITER_2( "ARBITER_2", "داور سطح 2"),
    ROLE_ARBITER_1( "ARBITER_1", "داور سطح 1"),
    ROLE_MANAGER_3( "MANAGER_3", "مدیر سطح 3"),
    ROLE_MANAGER_2( "MANAGER_2", "مدیر سطح 2"),
    ROLE_MANAGER_1( "MANAGER_1", "مدیر سطح 1");

    private final String title;
    private final String description;

    BaseUserRole(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
