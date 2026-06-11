package com.xxx.atrs.flight.controller;

import com.xxx.atrs.common.config.CommonResponse;
import com.xxx.atrs.common.constant.SeatClass;
import com.xxx.atrs.common.entity.Flight;
import com.xxx.atrs.common.entity.FlightSeat;
import com.xxx.atrs.flight.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/flight/admin")
@RequiredArgsConstructor
public class AdminFlightController {

    private final FlightService flightService;

    @GetMapping("/list")
    public CommonResponse<List<Flight>> list() {
        return CommonResponse.success(flightService.listAll());
    }

    @GetMapping("/detail/{id}")
    public CommonResponse<Flight> detail(@PathVariable Long id) {
        return CommonResponse.success(flightService.getDetailWithSeats(id));
    }

    @PostMapping("/add")
    public CommonResponse<String> add(@RequestBody Flight flight) {
        // 确保 flight-seat 关联
        if (flight.getSeats() != null) {
            for (FlightSeat seat : flight.getSeats()) {
                seat.setFlight(flight);
            }
        }
        flightService.save(flight);
        return CommonResponse.success("添加成功");
    }

    @PutMapping("/edit/{id}")
    public CommonResponse<String> edit(@PathVariable Long id, @RequestBody Flight update) {
        Flight existing = flightService.getDetailWithSeats(id);
        existing.setFlightNo(update.getFlightNo());
        existing.setAirline(update.getAirline());
        existing.setDepartureCity(update.getDepartureCity());
        existing.setArrivalCity(update.getArrivalCity());
        existing.setDepartureAirport(update.getDepartureAirport());
        existing.setArrivalAirport(update.getArrivalAirport());
        existing.setDepartureTime(update.getDepartureTime());
        existing.setArrivalTime(update.getArrivalTime());
        existing.setDurationMinutes(update.getDurationMinutes());
        existing.setFlightDate(update.getFlightDate());
        existing.setBasePrice(update.getBasePrice());
        if (update.getStatus() != null) existing.setStatus(update.getStatus());
        flightService.save(existing);
        return CommonResponse.success("编辑成功");
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponse<String> delete(@PathVariable Long id) {
        flightService.delete(id);
        return CommonResponse.success("删除成功");
    }

    @GetMapping("/statistics")
    public CommonResponse<?> statistics() {
        long total = flightService.listAll().size();
        return CommonResponse.success(java.util.Map.of("totalFlights", total));
    }
}
