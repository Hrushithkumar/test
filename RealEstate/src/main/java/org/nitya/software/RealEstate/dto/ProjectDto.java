package org.nitya.software.RealEstate.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectDto {
    private Long id;
    private String category;
    private String title;
    private String description;
    private String image;
}
