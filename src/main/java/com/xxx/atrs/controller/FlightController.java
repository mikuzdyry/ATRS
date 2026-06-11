package com.xxx.atrs.controller;

import com.xxx.atrs.dto.FlightSearchDTO;
import com.xxx.atrs.entity.Flight;
import com.xxx.atrs.service.FlightService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<String> departureCities = flightService.getDepartureCities();
        List<String> arrivalCities = flightService.getArrivalCities();
        model.addAttribute("departureCities", departureCities);
        model.addAttribute("arrivalCities", arrivalCities);
        return "index";
    }

    @GetMapping("/flight/search")
    public String search(FlightSearchDTO dto, Model model) {
        try {
            List<Flight> flights = flightService.searchFlights(dto);
            model.addAttribute("flights", flights);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("departureCity", dto.getDepartureCity());
        model.addAttribute("arrivalCity", dto.getArrivalCity());
        return "flight/search";
    }

    @GetMapping("/flight/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Flight flight = flightService.getDetailWithSeats(id);
        model.addAttribute("flight", flight);
        return "flight/detail";
    }
}
