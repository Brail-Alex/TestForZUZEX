package com.example.service;

import com.example.exception.EntityAlreadyExistException;
import com.example.model.User;
import com.example.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepo;

    @Autowired
    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public String  createUser(User user) throws EntityAlreadyExistException {
        User registeredUser = userRepo.save(user);
        log.info("IN createUser - user: {} successfully registered", registeredUser);
        return null;
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

    public User getUserByName(String name){
        User user = userRepo.findByName(name);
        if (user == null){
            log.info("IN getUserByName - no user found by name: {}", name);
            throw new EntityNotFoundException("User with this name not found.");
        }

        log.info("IN getUserById - user: {} found by name: {}", user, name);
        return user;
    }
    public User updateUser(User user, Long id) throws EntityNotFoundException {

        return userRepo.findById(id)
                .map(oldUser -> {
                    oldUser.setName(user.getName());
                    oldUser.setAge(user.getAge());
                    oldUser.setPassword(user.getPassword());

                    if(user.getHouse() != null) {
                        oldUser.setHouse(user.getHouse());
                    }
                    if (user.getHouseList() != null){
                        oldUser.setHouseList(user.getHouseList());
                    }
                    return userRepo.save(oldUser);
                })
                .orElseThrow(() -> {
                    log.info("IN getUserById - no user found by id: {}", id);
                    return new EntityNotFoundException("User with this id not found.");
                });
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

}
