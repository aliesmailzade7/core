package com.cybercenter.core.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class testController {
    @GetMapping("/1")
    public ResponseEntity<?> changeLoginMethod() {
        return ResponseEntity.accepted().build();
    }

}
