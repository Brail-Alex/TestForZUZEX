package com.example.service;

import com.example.exception.EntityAlreadyExistException;
import com.example.model.House;
import com.example.repository.HouseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class HouseService {

    private final HouseRepository houseRepo;
    private final UserService userService;

    @Autowired
    public HouseService(HouseRepository houseRepo, UserService userService) {
        this.houseRepo = houseRepo;
        this.userService = userService;
    }

    public House createHouse(House house) throws EntityAlreadyExistException {

        if (houseRepo.findByAddress(house.getAddress()) != null) {
            throw new EntityAlreadyExistException("House with this address already exist!");
        }

        House newHouse = houseRepo.save(house);
        log.info("IN createHouse - hose: {} successfully created", newHouse);
        return newHouse;
    }

    public List<House> getAllHouses() {
        List<House> houses = houseRepo.findAll();
        log.info("IN getAllHouses - {} users found", houses.size());
        return houses;
    }

    public House getHouseById(Long id) throws EntityNotFoundException {
        House house = houseRepo.findById(id).orElseThrow(() -> {
            log.info("IN getHouseById - no house found by id: {}", id);
            return new EntityNotFoundException("House with this id not found.");
        });

        log.info("IN getHouseById - house: {} found by id: {}", house, id);
        return house;
    }

    public House updateHouse(House house, Long id) throws EntityNotFoundException {

        return houseRepo.findById(id)
                .map(oldHouse -> {
                    oldHouse.setAddress(house.getAddress());
                    if (house.getOwnerId() != null) {
                        oldHouse.setOwnerId(house.getOwnerId());
                    }
                    if (house.getResidents() != null) {
                        oldHouse.setResidents(house.getResidents());
                    }
                    return houseRepo.save(oldHouse);
                })
                .orElseThrow(() -> {
                    log.info("IN getHouseById - no house found by id: {}", id);
                    return new EntityNotFoundException("House with this id not found.");
                });
    }

    public House updateHouse(House house) throws EntityNotFoundException {

        House oldHouse = houseRepo.findById(house.getId()).orElseThrow();
        oldHouse.setAddress(house.getAddress());
        if (house.getOwnerId() != null) {
            oldHouse.setOwnerId(house.getOwnerId());
        }
        if (house.getResidents() != null) {
            oldHouse.setResidents(house.getResidents());
        }
        return houseRepo.save(oldHouse);
    }

    public Long deleteHouseById(Long id) {
        House house = getHouseById(id);
        if (house != null) {
            houseRepo.delete(house);
        } else {
            log.info("IN getHouseById - no house found by id: {}", id);
            throw new EntityNotFoundException("House with this id not found.");
        }
        return id;
    }

    public House houseWithOwnerBuilder(String address, Long ownerId){
        return new House(address, userService.getUserById(ownerId));
    }

}
