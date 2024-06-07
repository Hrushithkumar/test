package org.nitya.software.RealEstate.model.enums;

import lombok.Getter;

@Getter
public enum Category {
    ONE_BHK_APARTMENT("1 BHK Apartment"),
    TWO_BHK_APARTMENT("2 BHK Apartment"),
    THREE_BHK_APARTMENT("3 BHK Apartment"),
    COMMERCIAL("Commercial"),
    INDUSTRIAL("Industrial");

    private final String categoryName;

    Category(String categoryName) {
        this.categoryName = categoryName;
    }

}
