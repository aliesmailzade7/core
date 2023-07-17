package com.cybercenter.core.handler;

import com.cybercenter.core.dto.ExceptionResponseDTO;
import com.cybercenter.core.exception.base.AbstractException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandling extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AbstractException.class)
    ResponseEntity<Object> handleAbstractExceptions(AbstractException e) {
        ExceptionResponseDTO response = new ExceptionResponseDTO();
        response.setDateTime(LocalDateTime.now());
        response.setMessage(e.getMessage());
        response.setStatus(e.getStatus());
        ResponseEntity<Object> entity = new ResponseEntity<>(response, HttpStatus.valueOf(e.getStatus()));
        return entity;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<ExceptionResponseDTO.ApiValidationError> subErrors = new ArrayList<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            ExceptionResponseDTO.ApiValidationError apiValidationError = ExceptionResponseDTO.ApiValidationError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .rejectedValue(fieldError.getRejectedValue())
                    .object(fieldError.getObjectName())
                    .build();

            subErrors.add(apiValidationError);
        }

        ExceptionResponseDTO response = new ExceptionResponseDTO();
        response.setDateTime(LocalDateTime.now());
        response.setMessage("Method Argument Not Valid");
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setValidationErrors(subErrors);
        return ResponseEntity.badRequest().body(response);
    }


}