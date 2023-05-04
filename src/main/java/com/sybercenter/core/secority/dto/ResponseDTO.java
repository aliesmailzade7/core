package com.sybercenter.core.secority.dto;

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
}
