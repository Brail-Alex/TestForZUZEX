package com.example.service;

import com.example.dto.AuthRequestsDto;
import com.example.exception.AuthorizationNotPass;
import com.example.exception.EntityAlreadyExistException;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.security.jwt.JwtToken;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepo;
    private final JwtToken jwtToken;

    @Autowired
    public UserService(UserRepository userRepo, JwtToken jwtToken) {
        this.userRepo = userRepo;
        this.jwtToken = jwtToken;
    }

    public String createUser(User user) throws EntityAlreadyExistException {
        if (getUserByName(user.getName()) == null) {
            User registeredUser = userRepo.save(user);
            log.info("IN createUser - user: {} successfully created", registeredUser);
            return jwtToken.createToken(user.getName());
        } else {
            log.info("IN createUser - user: {} with this name: {} already exist", user, user.getName());
            throw new EntityAlreadyExistException("User with this name already exist");
        }
    }

    public List<User> getAllUsers() {
        List<User> users = userRepo.findAll();
        log.info("IN getAllUsers - {} users found", users.size());
        return users;
    }

    public User getUserById(Long id) throws EntityNotFoundException {
        User user = userRepo.findById(id).orElseThrow(() -> {
            log.info("IN getUserById - no user found by id: {}", id);
            return new EntityNotFoundException("User with this id not found.");
        });

        log.info("IN getUserById - user: {} found by id: {}", user, id);
        return user;
    }

    public User getUserByName(String name) {
        User user = userRepo.findByName(name);
        if (user == null) {
            log.info("IN getUserByName - no user found by name: {}", name);
            throw new EntityNotFoundException("User with this name not found.");
        }

        log.info("IN getUserByName - user: {} found by name: {}", user, name);
        return user;
    }

    public User updateUser(User user, Long id) throws EntityNotFoundException {

        return userRepo.findById(id)
                .map(oldUser -> {
                    oldUser.setName(user.getName());
                    oldUser.setAge(user.getAge());
                    oldUser.setPassword(user.getPassword());

                    if (user.getHousesResidence() != null) {
                        oldUser.setHousesResidence(user.getHousesResidence());
                    }
                    if (user.getHouseOwnerList() != null) {
                        oldUser.setHouseOwnerList(user.getHouseOwnerList());
                    }
                    return userRepo.save(oldUser);
                })
                .orElseThrow(() -> {
                    log.info("IN getUserById - no user found by id: {}", id);
                    return new EntityNotFoundException("User with this id not found.");
                });
    }

    public User updateUser(User user) throws EntityNotFoundException {

        User oldUser = userRepo.findById(user.getId()).orElseThrow();
        oldUser.setName(user.getName());
        oldUser.setAge(user.getAge());
        oldUser.setPassword(user.getPassword());

        if (user.getHousesResidence() != null) {
            oldUser.setHousesResidence(user.getHousesResidence());
        }
        if (user.getHouseOwnerList() != null) {
            oldUser.setHouseOwnerList(user.getHouseOwnerList());
        }

        return userRepo.save(oldUser);
    }

    public Long deleteUserById(Long id) {
        User user = getUserById(id);
        if (user != null) {
            userRepo.delete(user);
        } else {
            log.info("IN getUserById - no user found by id: {}", id);
            throw new EntityNotFoundException("User with this id not found.");
        }
        return id;
    }

    /**
     * AUTHORIZATION
     */

    public String authorization(AuthRequestsDto auth) throws AuthorizationNotPass {
        User user = getUserByName(auth.getName());
        if (Objects.equals(user.getPassword(), auth.getPassword())) {
            return jwtToken.createToken(user.getName());
        } else {
            throw new AuthorizationNotPass("Enter the correct username or password!");
        }
    }


}
