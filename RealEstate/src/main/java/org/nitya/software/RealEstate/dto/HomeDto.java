package org.nitya.software.RealEstate.dto;

import lombok.Data;

@Data
public class HomeDto {
    private Long id;
    private String category;
    private String title;
    private String description;
    private String image;
}