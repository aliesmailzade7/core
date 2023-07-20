package com.cybercenter.core.controller;

import com.cybercenter.core.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/manager")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@SecurityRequirement(name = "Bearer Authentication")
public class ManagerController extends BaseController {

    UserService userService;

    @GetMapping("/report/users")
    public ResponseEntity<?> getUserList(Pageable pageable) {
        return ResponseEntity.ok().body(userService.getAll(pageable));
    }


}
