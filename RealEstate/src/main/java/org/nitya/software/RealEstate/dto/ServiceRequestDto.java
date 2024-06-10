package org.nitya.software.RealEstate.dto;

import lombok.Data;

@Data
public class ServiceRequestDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String serviceRequested;
    private String description;
    private String location;
    private Long userID;
    private String status;
}
