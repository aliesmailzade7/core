package com.cybercenter.core.controller;

import com.cybercenter.core.dto.ScopeDTO;
import com.cybercenter.core.service.ScopeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/scopes")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@SecurityRequirement(name = "Bearer Authentication")
public class ScopeController {

    ScopeService scopeService;

    @PostMapping
    public ResponseEntity<?> setScope(@RequestBody ScopeDTO dto) {
        return ResponseEntity.ok(scopeService.save(dto));
    }

    @PutMapping("/{scopeId}")
    public ResponseEntity<?> updateScope(@PathVariable int scopeId, @RequestBody ScopeDTO dto) {
        return ResponseEntity.ok(scopeService.update(scopeId, dto));
    }

    @GetMapping
    public ResponseEntity<?> getScopes() {
        return ResponseEntity.ok(scopeService.findAll());
    }

    @GetMapping("/{scopeId}")
    public ResponseEntity<?> getScope(@PathVariable int scopeId) {
        return ResponseEntity.ok(scopeService.findById(scopeId));
    }

}
