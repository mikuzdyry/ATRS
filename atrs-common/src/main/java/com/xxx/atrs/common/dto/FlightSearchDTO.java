package com.xxx.atrs.common.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class FlightSearchDTO {
    private String departureCity;
    private String arrivalCity;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
