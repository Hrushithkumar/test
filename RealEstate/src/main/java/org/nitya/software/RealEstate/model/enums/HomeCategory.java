package org.nitya.software.RealEstate.model.enums;

import lombok.Getter;

@Getter
public enum HomeCategory {
    LUXURY_VILLA("Luxury Villa"),
    TWO_BHK_APARTMENT("Modern Apartment"),
    THREE_BHK_APARTMENT("Cozy Cottage");

    private final String categoryName;

    HomeCategory(String categoryName) {
        this.categoryName = categoryName;
    }
}
