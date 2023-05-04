package com.sybercenter.core.secority.exception;

public abstract class AbstractException extends RuntimeException {

    public abstract String getMessage();

    public abstract int getStatus();
}
