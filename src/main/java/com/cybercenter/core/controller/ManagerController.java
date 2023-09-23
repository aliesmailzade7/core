package com.cybercenter.core.controller;

import com.cybercenter.core.constant.Authority;
import com.cybercenter.core.dto.UserInfoDTO;
import com.cybercenter.core.service.RoleService;
import com.cybercenter.core.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@SecurityRequirement(name = "Bearer Authentication")
public class ManagerController extends BaseController {

    UserService userService;
    RoleService roleService;

    @GetMapping
    public ResponseEntity<?> getUserList(Pageable pageable, HttpServletRequest request) {
        checkAuthorities(Authority.OP_USER_REPORT.name(), request);
        return ResponseEntity.ok().body(userService.findAll(pageable));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserInfo(@PathVariable long userId, HttpServletRequest request) {
        checkAuthorities(Authority.OP_USER_REPORT.name(), request);
        return ResponseEntity.ok().body(userService.findById(userId));
    }

    @PostMapping("/{userId}/block-unblock")
    public ResponseEntity<?> blockAndUnblockUser(@PathVariable long userId, HttpServletRequest request) {
        checkAuthorities(Authority.OP_USER_BLOCK_AND_UNBLOCK.name(), request);
        userService.blockAndUnblockUser(userId);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserInfo(@PathVariable long userId, @RequestBody UserInfoDTO userInfoDTO, HttpServletRequest request) {
        checkAuthorities(Authority.OP_UPDATE_USER_INFO.name(), request);
        userService.updateProfile(userId, userInfoDTO);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{userId}/change-password")
    public ResponseEntity<?> changeUserPassword(@PathVariable long userId, @RequestParam String newPassword, HttpServletRequest request) {
        checkAuthorities(Authority.OP_CHANGE_USER_PASS.name(), request);
        userService.setNewPassword(userId, newPassword);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/roles")
    public ResponseEntity<?> getRoleList() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @PostMapping("/{userId}/roles")
    public ResponseEntity<?> addRole(@PathVariable long userId, @RequestParam List<Integer> roleIds, HttpServletRequest request) {
        checkAuthorities(Authority.OP_ADD_USER_ROLE.name(), request);
        userService.addRole(userId, roleIds);
        return ResponseEntity.accepted().build();
    }
}
