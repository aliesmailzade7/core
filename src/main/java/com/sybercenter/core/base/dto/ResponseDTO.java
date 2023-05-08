package com.sybercenter.core.base.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseDTO<T> {
    private Integer status;
    private String message;
    private T data;

    public ResponseDTO(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
}
