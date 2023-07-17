package com.cybercenter.core.constant;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {
    OP_ACCESS_ADMIN,
    OP_ACCESS_USER;

    @Override
    public String getAuthority() {
        return this.name();
    }
}