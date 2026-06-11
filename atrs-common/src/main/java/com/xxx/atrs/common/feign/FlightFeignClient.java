package com.xxx.atrs.common.feign;

import com.xxx.atrs.common.config.CommonResponse;
import com.xxx.atrs.common.entity.Flight;
import com.xxx.atrs.common.entity.FlightSeat;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "atrs-flight-service", path = "/api/flight")
public interface FlightFeignClient {

    @GetMapping("/{id}")
    CommonResponse<Flight> getFlight(@PathVariable Long id);

    @PostMapping("/internal/deductSeats")
    CommonResponse<Void> deductSeats(@RequestBody Map<String, Object> request);

    @PostMapping("/internal/releaseSeats")
    CommonResponse<Void> releaseSeats(@RequestBody Map<String, Object> request);

    @GetMapping("/internal/seats/{flightId}/{seatClass}")
    CommonResponse<FlightSeat> getSeatInventory(@PathVariable Long flightId, @PathVariable String seatClass);
}
