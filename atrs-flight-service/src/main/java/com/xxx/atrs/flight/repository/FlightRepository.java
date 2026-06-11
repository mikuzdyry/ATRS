package com.xxx.atrs.flight.repository;

import com.xxx.atrs.common.constant.FlightStatus;
import com.xxx.atrs.common.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findByDepartureCityAndArrivalCityAndFlightDateAndStatus(
            String departureCity, String arrivalCity, LocalDate date, FlightStatus status);

    List<Flight> findByDepartureCityAndArrivalCityAndStatus(
            String departureCity, String arrivalCity, FlightStatus status);

    @Query("SELECT DISTINCT f.departureCity FROM Flight f WHERE f.status = 'SCHEDULED'")
    List<String> findDistinctDepartureCities();

    @Query("SELECT DISTINCT f.arrivalCity FROM Flight f WHERE f.status = 'SCHEDULED'")
    List<String> findDistinctArrivalCities();

    List<Flight> findByFlightDateAndStatus(LocalDate date, FlightStatus status);
}
