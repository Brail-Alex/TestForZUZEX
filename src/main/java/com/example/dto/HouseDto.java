package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HouseDto {
    private String address;
    private Long ownerId;

}
