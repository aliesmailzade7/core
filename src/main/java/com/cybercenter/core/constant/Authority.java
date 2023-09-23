package com.cybercenter.core.constant;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {
    OP_USER_REPORT,
    OP_USER_BLOCK_AND_UNBLOCK,
    OP_UPDATE_USER_INFO,
    OP_CHANGE_USER_PASS,
    OP_ADD_USER_ROLE,
    OP_ACCESS_USER;

    @Override
    public String getAuthority() {
        return this.name();
    }
}