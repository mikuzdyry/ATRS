package com.xxx.atrs.flight.service;

import com.xxx.atrs.common.constant.SeatClass;
import com.xxx.atrs.common.entity.Flight;
import com.xxx.atrs.common.entity.FlightSeat;
import com.xxx.atrs.common.exception.BusinessException;
import com.xxx.atrs.flight.repository.FlightRepository;
import com.xxx.atrs.flight.repository.FlightSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;
    private final FlightSeatRepository flightSeatRepository;

    public List<Flight> searchFlights(String departureCity, String arrivalCity, String dateStr) {
        if (departureCity == null || departureCity.isBlank() || arrivalCity == null || arrivalCity.isBlank()) {
            throw new BusinessException("出发城市和到达城市不能为空");
        }
        if (dateStr != null && !dateStr.isBlank()) {
            var date = java.time.LocalDate.parse(dateStr);
            return flightRepository.findByDepartureCityAndArrivalCityAndFlightDateAndStatus(
                    departureCity, arrivalCity, date, com.xxx.atrs.common.constant.FlightStatus.SCHEDULED);
        }
        return flightRepository.findByDepartureCityAndArrivalCityAndStatus(
                departureCity, arrivalCity, com.xxx.atrs.common.constant.FlightStatus.SCHEDULED);
    }

    public Flight getDetailWithSeats(Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new BusinessException("航班不存在"));
        List<FlightSeat> seats = flightSeatRepository.findByFlightId(flightId);
        flight.setSeats(seats);
        return flight;
    }

    public List<String> getDepartureCities() {
        return flightRepository.findDistinctDepartureCities();
    }

    public List<String> getArrivalCities() {
        return flightRepository.findDistinctArrivalCities();
    }

    public List<Flight> listAll() {
        return flightRepository.findAll();
    }

    @Transactional
    public Flight save(Flight flight) {
        return flightRepository.save(flight);
    }

    @Transactional
    public void delete(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new BusinessException("航班不存在"));
        flightRepository.delete(flight);
    }

    public Flight getById(Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new BusinessException("航班不存在"));
    }

    // === 内部接口：座位扣减/释放 ===

    @Transactional
    public void deductSeats(Long flightId, String seatClass, int count) {
        SeatClass sc = SeatClass.valueOf(seatClass);
        FlightSeat seat = flightSeatRepository.findByFlightIdAndSeatClass(flightId, sc)
                .orElseThrow(() -> new BusinessException("座位库存不存在"));
        if (seat.getAvailableSeats() < count) {
            throw new BusinessException("座位不足，当前可用：" + seat.getAvailableSeats());
        }
        seat.setBookedCount(seat.getBookedCount() + count);
        flightSeatRepository.save(seat);
    }

    @Transactional
    public void releaseSeats(Long flightId, String seatClass, int count) {
        SeatClass sc = SeatClass.valueOf(seatClass);
        FlightSeat seat = flightSeatRepository.findByFlightIdAndSeatClass(flightId, sc)
                .orElseThrow(() -> new BusinessException("座位库存不存在"));
        seat.setBookedCount(Math.max(0, seat.getBookedCount() - count));
        flightSeatRepository.save(seat);
    }

    public FlightSeat getSeatInventory(Long flightId, String seatClass) {
        SeatClass sc = SeatClass.valueOf(seatClass);
        return flightSeatRepository.findByFlightIdAndSeatClass(flightId, sc)
                .orElseThrow(() -> new BusinessException("座位库存不存在"));
    }
}
