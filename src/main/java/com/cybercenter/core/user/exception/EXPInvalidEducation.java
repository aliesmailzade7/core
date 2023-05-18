package com.cybercenter.core.user.exception;

import com.cybercenter.core.base.exception.AbstractBadRequestException;

public class EXPInvalidEducation extends AbstractBadRequestException {
    @Override
    public String getMessage() {
        return "سطح تحصیلات نامعتبر است.";
    }
}
