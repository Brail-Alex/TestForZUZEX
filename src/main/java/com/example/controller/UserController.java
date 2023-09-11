package com.example.controller;

import com.example.dto.HouseDto;
import com.example.exception.EntityAlreadyExistException;
import com.example.exception.JwtAuthenticationException;
import com.example.model.User;
import com.example.security.jwt.JwtTokenCheck;
import com.example.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final JwtTokenCheck jwtTokenCheck;

    @Autowired
    public UserController(UserService userService, JwtTokenCheck jwtTokenCheck) {
        this.userService = userService;
        this.jwtTokenCheck = jwtTokenCheck;
    }

    @PostMapping
    public ResponseEntity registrationUser(@RequestBody User user) {
        try {
            userService.createUser(user);
            return ResponseEntity.ok().body("User registered");
        } catch (EntityAlreadyExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error occurred");
        }
    }

    @GetMapping(params = {"id"})
    public ResponseEntity getUserById(@RequestParam Long id,
                                      @RequestHeader("token") String token) {
        try {
            jwtTokenCheck.checkToken(token);
        } catch (JwtAuthenticationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error occurred");
        }
    }

    @GetMapping(params = {"name"})
    public ResponseEntity getUserByName(@RequestParam String name,
                                        @RequestHeader("token") String token) {
        try {
            jwtTokenCheck.checkToken(token);
        } catch (JwtAuthenticationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        try {
            return ResponseEntity.ok(userService.getUserByName(name));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error occurred");
        }
    }

    @GetMapping
    public ResponseEntity getAllUsers(@RequestHeader("token") String token) {
        try {
            jwtTokenCheck.checkToken(token);
        } catch (JwtAuthenticationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        try {
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error occurred");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id,
                                     @RequestHeader("token") String token) {
        try {
            jwtTokenCheck.checkToken(token);
        } catch (JwtAuthenticationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        try {
            return ResponseEntity.ok(userService.deleteUserById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error occurred");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updateUser(@RequestBody User user,
                                     @PathVariable Long id,
                                     @RequestHeader("token") String token) {
        try {
            jwtTokenCheck.checkToken(token);
        } catch (JwtAuthenticationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        try {
            userService.updateUser(user, id);
            return ResponseEntity.ok().body("User updated");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error occurred");
        }
    }

}
