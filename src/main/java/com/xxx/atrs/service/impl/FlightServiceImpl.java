package com.xxx.atrs.service.impl;

import com.xxx.atrs.common.constant.FlightStatus;
import com.xxx.atrs.common.exception.BusinessException;
import com.xxx.atrs.dto.FlightSearchDTO;
import com.xxx.atrs.entity.Flight;
import com.xxx.atrs.entity.FlightSeat;
import com.xxx.atrs.repository.BookingOrderRepository;
import com.xxx.atrs.repository.FlightRepository;
import com.xxx.atrs.repository.FlightSeatRepository;
import com.xxx.atrs.service.FlightService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final FlightSeatRepository flightSeatRepository;
    private final BookingOrderRepository bookingOrderRepository;

    public FlightServiceImpl(FlightRepository flightRepository, FlightSeatRepository flightSeatRepository,
                             BookingOrderRepository bookingOrderRepository) {
        this.flightRepository = flightRepository;
        this.flightSeatRepository = flightSeatRepository;
        this.bookingOrderRepository = bookingOrderRepository;
    }

    @Override
    public List<Flight> searchFlights(FlightSearchDTO dto) {
        if (dto.getDepartureCity() == null || dto.getArrivalCity() == null
                || dto.getDepartureCity().isBlank() || dto.getArrivalCity().isBlank()) {
            throw new BusinessException("出发城市和到达城市不能为空");
        }
        if (dto.getDate() != null) {
            return flightRepository.findByDepartureCityAndArrivalCityAndFlightDateAndStatus(
                    dto.getDepartureCity(), dto.getArrivalCity(), dto.getDate(), FlightStatus.SCHEDULED);
        }
        return flightRepository.findByDepartureCityAndArrivalCityAndStatus(
                dto.getDepartureCity(), dto.getArrivalCity(), FlightStatus.SCHEDULED);
    }

    @Override
    public Flight getDetailWithSeats(Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new BusinessException("航班不存在"));
        List<FlightSeat> seats = flightSeatRepository.findByFlightId(flightId);
        flight.setSeats(seats);
        return flight;
    }

    @Override
    public List<String> getDepartureCities() {
        return flightRepository.findDistinctDepartureCities();
    }

    @Override
    public List<String> getArrivalCities() {
        return flightRepository.findDistinctArrivalCities();
    }

    @Override
    @Transactional
    public Flight save(Flight flight) {
        return flightRepository.save(flight);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        long orderCount = bookingOrderRepository.countByFlightId(id);
        if (orderCount > 0) {
            throw new BusinessException("该航班已有 " + orderCount + " 个订单，无法删除");
        }
        flightRepository.deleteById(id);
    }

    @Override
    public List<Flight> listAll() {
        return flightRepository.findAll();
    }
}
