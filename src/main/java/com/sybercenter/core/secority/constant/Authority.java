package com.sybercenter.core.secority.constant;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {
    OP_ACCESS_ADMIN,
    OP_INSERT_NEW_USER;

    @Override
    public String getAuthority() {
        return this.name();
    }
}