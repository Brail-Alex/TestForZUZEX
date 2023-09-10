package com.example.controller;

import com.example.dto.AuthRequestsDto;
import com.example.exception.AuthorizationNotPass;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AuthorizationController {
    private final UserService userService;

    @Autowired
    public AuthorizationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/auth")
    public ResponseEntity authorization(@RequestBody AuthRequestsDto authRequestsDto) {
        try {
            return ResponseEntity.ok(userService.authorization(authRequestsDto));
        } catch (AuthorizationNotPass anp) {
            return ResponseEntity.badRequest().body(anp.getMessage());
        }

    }
}
