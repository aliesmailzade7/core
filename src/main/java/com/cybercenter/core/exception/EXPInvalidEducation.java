package com.cybercenter.core.exception;

import com.cybercenter.core.exception.base.AbstractBadRequestException;

public class EXPInvalidEducation extends AbstractBadRequestException {
    @Override
    public String getMessage() {
        return "سطح تحصیلات نامعتبر است.";
    }
}
