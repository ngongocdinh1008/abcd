package com.example.shoprunner_be.controllers;

import com.example.shoprunner_be.dtos.UserDTO;
import com.example.shoprunner_be.dtos.UserLoginDTO;
import com.example.shoprunner_be.entitys.User;
import com.example.shoprunner_be.responses.User.UserListResponse;
import com.example.shoprunner_be.responses.User.UserLoginResponse;
import com.example.shoprunner_be.responses.User.UserResponse;
import com.example.shoprunner_be.services.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO) {
        try {
            if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
                return ResponseEntity.badRequest().body("Passwords do not match");
            }
            User user = userService.createUser(userDTO);
            return ResponseEntity.ok(UserResponse.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .phoneNumber(user.getPhoneNumber())
                    .email(user.getEmail())
                    .address(user.getAddress())
                    .role(user.getRole().getName())
                    .build());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e + "Unable to register !!");
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @Valid
            @RequestBody
            UserLoginDTO userLoginDTO) {
        try {
            String token = userService.login(userLoginDTO);
            User user = userService.getUser(userLoginDTO);
            return ResponseEntity.ok(UserLoginResponse.builder()
                    .id(user.getId())
                    .token(token)
                    .build()
            );
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e + "Unable to login !!");
        }
    }
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllUsers(
            @RequestParam(defaultValue = "" , required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) throws Exception{
        try {
            Pageable pageable = PageRequest.of(page, limit);
            Page<User> userPage = userService.getAllUsers(keyword, pageable);
            UserListResponse userListResponse = UserListResponse.builder()
                    .userResponses(userPage.getContent().stream().map(user -> UserResponse.builder()
                                    .id(user.getId())
                                    .name(user.getName())
                                    .email(user.getEmail())
                                    .phoneNumber(user.getPhoneNumber())
                                    .role(user.getRole().getName())
                                    .build())
                            .toList())
                    .totalPages(userPage.getTotalPages())
                    .build();
            return ResponseEntity.ok(userListResponse);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e + "Unable to get users !!");
        }
}

    @PostMapping("/block/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> blockUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user.getRole().getName().equals("USER")) {
            if (user.isActive()) {
                userService.blockUser(id);
            } else {
                userService.unblockUser(id);
            }
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("You are not allowed to block this User");
        }
    }
}