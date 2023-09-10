package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddResidentDto {
    private Long futureResidentId;
    private String address;
}
