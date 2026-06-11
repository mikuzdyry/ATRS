package com.xxx.atrs.dto;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

public class FlightSearchDTO {
    private String departureCity;
    private String arrivalCity;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    public String getDepartureCity() { return departureCity; }
    public void setDepartureCity(String departureCity) { this.departureCity = departureCity; }

    public String getArrivalCity() { return arrivalCity; }
    public void setArrivalCity(String arrivalCity) { this.arrivalCity = arrivalCity; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}
