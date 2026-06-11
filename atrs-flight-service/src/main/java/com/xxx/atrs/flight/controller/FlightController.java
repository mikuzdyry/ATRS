package com.xxx.atrs.flight.controller;

import com.xxx.atrs.common.config.CommonResponse;
import com.xxx.atrs.common.entity.Flight;
import com.xxx.atrs.flight.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/flight")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @GetMapping("/search")
    public CommonResponse<List<Flight>> search(
            @RequestParam String departureCity,
            @RequestParam String arrivalCity,
            @RequestParam(required = false) String date) {
        return CommonResponse.success(flightService.searchFlights(departureCity, arrivalCity, date));
    }

    @GetMapping("/{id}")
    public CommonResponse<Flight> detail(@PathVariable Long id) {
        return CommonResponse.success(flightService.getDetailWithSeats(id));
    }

    @GetMapping("/departureCities")
    public CommonResponse<List<String>> departureCities() {
        return CommonResponse.success(flightService.getDepartureCities());
    }

    @GetMapping("/arrivalCities")
    public CommonResponse<List<String>> arrivalCities() {
        return CommonResponse.success(flightService.getArrivalCities());
    }

    // === 内部接口 ===

    @GetMapping("/internal/{id}")
    public CommonResponse<Flight> getFlight(@PathVariable Long id) {
        return CommonResponse.success(flightService.getById(id));
    }

    @PostMapping("/internal/deductSeats")
    public CommonResponse<Void> deductSeats(@RequestBody Map<String, Object> request) {
        Long flightId = ((Number) request.get("flightId")).longValue();
        String seatClass = (String) request.get("seatClass");
        int count = (Integer) request.get("count");
        flightService.deductSeats(flightId, seatClass, count);
        return CommonResponse.success();
    }

    @PostMapping("/internal/releaseSeats")
    public CommonResponse<Void> releaseSeats(@RequestBody Map<String, Object> request) {
        Long flightId = ((Number) request.get("flightId")).longValue();
        String seatClass = (String) request.get("seatClass");
        int count = (Integer) request.get("count");
        flightService.releaseSeats(flightId, seatClass, count);
        return CommonResponse.success();
    }

    @GetMapping("/internal/seats/{flightId}/{seatClass}")
    public CommonResponse<?> getSeatInventory(@PathVariable Long flightId, @PathVariable String seatClass) {
        return CommonResponse.success(flightService.getSeatInventory(flightId, seatClass));
    }
}
