package com.xxx.atrs.repository;

import com.xxx.atrs.common.constant.SeatClass;
import com.xxx.atrs.entity.FlightSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FlightSeatRepository extends JpaRepository<FlightSeat, Long> {
    List<FlightSeat> findByFlightId(Long flightId);
    Optional<FlightSeat> findByFlightIdAndSeatClass(Long flightId, SeatClass seatClass);
}
