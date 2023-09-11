package com.example.service;

import com.example.exception.EntityAlreadyExistException;
import com.example.model.House;
import com.example.model.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OwnerService {
    private final UserService userService;
    private final HouseService houseService;

    @Autowired
    public OwnerService(UserService userService, HouseService houseService) {
        this.userService = userService;
        this.houseService = houseService;
    }

    public String addResidentsAtHouse(User owner, User futureResident, String address) {
        List<House> ownerHouseList = owner.getHouseOwnerList();
        House theHouse = new House();
        for (House house : ownerHouseList) {
            if (house.getAddress().equals(address)) {
                theHouse = house;
            } else {
                return new EntityNotFoundException("House with this address from the owner not found.").getMessage();
            }
        }
        List<User> residentList = theHouse.getResidents() != null
                ? theHouse.getResidents()
                : new ArrayList<>();

        List<House> housesResidence = futureResident.getHousesResidence() != null
                ? futureResident.getHousesResidence()
                : new ArrayList<>();

        for (House house : housesResidence) {
            if(house.equals(theHouse)){
                return new EntityAlreadyExistException("User already residence at this the house").getMessage();
            }
        }

        residentList.add(futureResident);
        housesResidence.add(theHouse);
        futureResident.setHousesResidence(housesResidence);
        theHouse.setResidents(residentList);
        houseService.updateHouse(theHouse);
        userService.updateUser(futureResident);

        return "Resident added in house at the address " + address;
    }
}
