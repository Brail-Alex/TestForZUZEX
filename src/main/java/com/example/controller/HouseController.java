package com.example.controller;

import com.example.dto.HouseDto;
import com.example.exception.EntityAlreadyExistException;
import com.example.exception.JwtAuthenticationException;
import com.example.model.House;
import com.example.security.jwt.JwtTokenCheck;
import com.example.service.HouseService;
import com.example.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/house")
public class HouseController {

    private final HouseService houseService;
    private final UserService userService;
    private JwtTokenCheck jwtTokenCheck;

    @Autowired
    public HouseController(HouseService houseService, UserService userService, JwtTokenCheck jwtTokenCheck) {
        this.houseService = houseService;
        this.userService = userService;
        this.jwtTokenCheck = jwtTokenCheck;
    }

    /**
     * can add an AOP to validate the token(jwtTokenCheck.checkToken(token);)
     */

    @GetMapping(params = {"id"})
    public ResponseEntity getHouseById(@RequestParam Long id,
                                       @RequestHeader("token") String token) {
        try {
            jwtTokenCheck.checkToken(token);
        } catch (JwtAuthenticationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        try {
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
            jwtTokenCheck.checkToken(token);
        } catch (JwtAuthenticationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        try {
            return ResponseEntity.ok(houseService.getAllHouses());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error occurred");
        }
    }


    @PostMapping
    public ResponseEntity createHouse(@RequestBody HouseDto houseDto,
                                      @RequestHeader("token") String token) {
        try {
            jwtTokenCheck.checkToken(token);
        } catch (JwtAuthenticationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        try {
            houseService.createHouse(houseService.houseWithOwnerBuilder(houseDto.getAddress(), houseDto.getOwnerId()));
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
            jwtTokenCheck.checkToken(token);
        } catch (JwtAuthenticationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        try {
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
            jwtTokenCheck.checkToken(token);
        } catch (JwtAuthenticationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        try {
            houseService.updateHouse(house, id);
            return ResponseEntity.ok().body("House updated");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error occurred");
        }
    }

}
