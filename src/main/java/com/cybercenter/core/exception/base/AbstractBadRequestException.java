package com.cybercenter.core.exception.base;

public abstract class AbstractBadRequestException extends AbstractException {
    @Override
    public int getStatus() {
        return 400;
    }
}
