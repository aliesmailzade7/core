package com.cybercenter.core.base.exception;

public abstract class AbstractException extends RuntimeException {

    public abstract String getMessage();

    public abstract int getStatus();
}
