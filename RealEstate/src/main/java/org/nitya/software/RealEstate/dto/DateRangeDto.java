package org.nitya.software.RealEstate.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DateRangeDto {
    private LocalDate startDate;
    private LocalDate endDate;
}
