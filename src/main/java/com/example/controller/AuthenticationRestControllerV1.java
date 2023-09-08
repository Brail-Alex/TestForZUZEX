package com.example.controller;

import com.example.dto.AuthenticationRequestsDto;
import com.example.model.User;
import com.example.security.jwt.JwtTokenProvider;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "api/v1/auth/")

public class AuthenticationRestControllerV1 {
    @Autowired
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Autowired
    public AuthenticationRestControllerV1(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    public ResponseEntity login(@RequestBody AuthenticationRequestsDto requestsDto){
        try {
            String name = requestsDto.getName();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(name, requestsDto.getPassword()));
            User user = userService.getUserByName(name);
            if (user == null){
                throw new UsernameNotFoundException("User with username: " + name + " not found");
            }

            String token = jwtTokenProvider.createToken(name);
            Map<Object, Object> response = new HashMap<>();
            response.put("username", name);
            response.put("token", token);
            return ResponseEntity.ok(requestsDto);
        } catch (AuthenticationException e) {
        throw new BadCredentialsException("invalid username or password");
        }
    }
}
