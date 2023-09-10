package com.example.controller;

import com.example.exception.EntityAlreadyExistException;
import com.example.exception.JwtAuthenticationException;
import com.example.model.House;
import com.example.security.jwt.JwtTokenCheck;
import com.example.service.HouseService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/houses")
public class HouseController {

    private final HouseService houseService;
    private final JwtTokenCheck jwtTokenCheck;

    @Autowired
    public HouseController(HouseService houseService, JwtTokenCheck jwtTokenCheck) {
        this.houseService = houseService;
        this.jwtTokenCheck = jwtTokenCheck;
    }

    @GetMapping(params = {"id"})
    public ResponseEntity getHouseById(@RequestParam Long id,
                                      @RequestHeader("token") String token) {
        try {
            try {
                jwtTokenCheck.checkToken(token);
            } catch (JwtAuthenticationException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            return ResponseEntity.ok(houseService.getHouseById(id));

        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error occurred");
        }
    }

    @GetMapping
    public ResponseEntity getAllHouses(@RequestHeader("token") String token) {
        try {
            try {
                jwtTokenCheck.checkToken(token);
            } catch (JwtAuthenticationException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            return ResponseEntity.ok(houseService.getAllHouses());

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error occurred");
        }
    }


    @PostMapping
    public ResponseEntity createHouse(@RequestBody House house,
                                        @RequestHeader("token") String token) {

        try {
            try {
                jwtTokenCheck.checkToken(token);
            } catch (JwtAuthenticationException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            houseService.createHouse(house);
            return ResponseEntity.ok().body("House added");

        } catch (EntityAlreadyExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error occurred");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteHouse(@PathVariable Long id,
                                        @RequestHeader("token") String token) {
        try {
            try {
                jwtTokenCheck.checkToken(token);
            } catch (JwtAuthenticationException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            return ResponseEntity.ok(houseService.deleteHouseById(id));

        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error occurred");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updateHouse(@RequestBody House house,
                                        @PathVariable Long id,
                                        @RequestHeader("token") String token) {
        try {
            try {
                jwtTokenCheck.checkToken(token);
            } catch (JwtAuthenticationException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            houseService.updateHouse(house, id);
            return ResponseEntity.ok().body("House updated");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error occurred");
        }
    }

}
