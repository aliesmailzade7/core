package com.sybercenter.core.secority.exception;

public abstract class AbstractBadRequestException extends AbstractException{
    @Override
    public int getStatus() {
        return 400;
    }
}
