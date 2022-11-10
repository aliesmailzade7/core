package com.sybercenter.core.exception;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ExceptionResponse {
    private Integer status;
    private String message;
    private LocalDateTime dateTime;
    private List<ApiValidationError> validationErrors;

    @Data
    @Builder
    @AllArgsConstructor
    public static class ApiValidationError {
        private String object;
        private String field;
        private Object rejectedValue;
        private String message;
    }
}
