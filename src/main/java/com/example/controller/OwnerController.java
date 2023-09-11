package com.example.controller;

import com.example.dto.AddResidentDto;
import com.example.exception.JwtAuthenticationException;
import com.example.model.User;
import com.example.security.jwt.JwtTokenCheck;
import com.example.service.OwnerService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/owner")
public class OwnerController {
    private final OwnerService ownerService;
    private final UserService userService;
    private final JwtTokenCheck jwtTokenCheck;

    @Autowired
    public OwnerController(OwnerService ownerService, UserService userService, JwtTokenCheck jwtTokenCheck) {
        this.ownerService = ownerService;
        this.userService = userService;
        this.jwtTokenCheck = jwtTokenCheck;
    }

    @PutMapping
    public ResponseEntity addResident(@RequestBody AddResidentDto addResidentDto,
                                      @RequestHeader("token") String token) {
        String username = "";

        try {
            username = jwtTokenCheck.checkToken(token);
        } catch (JwtAuthenticationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        User owner = userService.getUserByName(username);
        User futureResident = userService.getUserById(addResidentDto.getFutureResidentId());

        try {
            return ResponseEntity.ok(ownerService.addResidentsAtHouse(owner, futureResident, addResidentDto.getAddress()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
