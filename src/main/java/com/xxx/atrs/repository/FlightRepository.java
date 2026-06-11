package com.xxx.atrs.repository;

import com.xxx.atrs.common.constant.FlightStatus;
import com.xxx.atrs.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findByDepartureCityAndArrivalCityAndFlightDateAndStatus(
            String departureCity, String arrivalCity, LocalDate flightDate, FlightStatus status);

    List<Flight> findByDepartureCityAndArrivalCityAndStatus(
            String departureCity, String arrivalCity, FlightStatus status);

    @Query("SELECT DISTINCT f.departureCity FROM Flight f WHERE f.status = 'SCHEDULED'")
    List<String> findDistinctDepartureCities();

    @Query("SELECT DISTINCT f.arrivalCity FROM Flight f WHERE f.status = 'SCHEDULED'")
    List<String> findDistinctArrivalCities();

    List<Flight> findByFlightDateAndStatus(LocalDate flightDate, FlightStatus status);
}
