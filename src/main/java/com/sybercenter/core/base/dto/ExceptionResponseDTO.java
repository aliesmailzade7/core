package com.sybercenter.core.base.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponseDTO {
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
