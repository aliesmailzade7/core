package com.sybercenter.core.secority.constant;

public enum UserRole {
    ROLE_ADMIN(1, "ADMIN"),
    ROLE_USER(2, "USER");

    private int id;
    private String name;

    UserRole(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
