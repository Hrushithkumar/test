package org.nitya.software.RealEstate.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProjectDto {
    private Long id;
    private String category;
    private String title;
    private String description;
    private List<String> images;
}
