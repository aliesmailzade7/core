package com.cybercenter.core.exception.base;

public abstract class AbstractException extends RuntimeException {

    public abstract String getMessage();

    public abstract int getStatus();
}
