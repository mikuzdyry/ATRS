package com.xxx.atrs.service;

import com.xxx.atrs.dto.FlightSearchDTO;
import com.xxx.atrs.entity.Flight;
import java.util.List;

public interface FlightService {
    List<Flight> searchFlights(FlightSearchDTO dto);
    Flight getDetailWithSeats(Long flightId);
    List<String> getDepartureCities();
    List<String> getArrivalCities();
    Flight save(Flight flight);
    void delete(Long id);
    List<Flight> listAll();
}
