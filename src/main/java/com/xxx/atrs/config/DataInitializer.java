package com.xxx.atrs.config;

import com.xxx.atrs.common.constant.FlightStatus;
import com.xxx.atrs.common.constant.SeatClass;
import com.xxx.atrs.entity.Flight;
import com.xxx.atrs.entity.FlightSeat;
import com.xxx.atrs.entity.User;
import com.xxx.atrs.repository.FlightRepository;
import com.xxx.atrs.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final FlightRepository flightRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, FlightRepository flightRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.flightRepository = flightRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Database already initialized, skipping seed data.");
            return;
        }
        log.info("Seeding demo data...");

        // Create users
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRealName("管理员");
        admin.setRole("ADMIN");
        userRepository.save(admin);

        User testUser = new User();
        testUser.setUsername("test");
        testUser.setPassword(passwordEncoder.encode("123456"));
        testUser.setRealName("测试用户");
        testUser.setPhone("13800138000");
        testUser.setEmail("test@example.com");
        userRepository.save(testUser);

        log.info("Users seeded.");

        // Flight route templates
        String[][] routes = {
            {"CA1501", "Air China", "Beijing", "Beijing Capital (PEK)", "Shanghai", "Shanghai Pudong (PVG)", "07:00", "09:15", "135"},
            {"CA1502", "Air China", "Shanghai", "Shanghai Pudong (PVG)", "Beijing", "Beijing Capital (PEK)", "10:00", "12:15", "135"},
            {"CZ3101", "China Southern", "Guangzhou", "Guangzhou Baiyun (CAN)", "Beijing", "Beijing Capital (PEK)", "08:30", "11:30", "180"},
            {"CZ3102", "China Southern", "Beijing", "Beijing Capital (PEK)", "Guangzhou", "Guangzhou Baiyun (CAN)", "13:00", "16:00", "180"},
            {"MU5201", "China Eastern", "Shanghai", "Shanghai Pudong (PVG)", "Shenzhen", "Shenzhen Baoan (SZX)", "09:00", "11:20", "140"},
            {"MU5202", "China Eastern", "Shenzhen", "Shenzhen Baoan (SZX)", "Shanghai", "Shanghai Pudong (PVG)", "12:30", "14:50", "140"},
            {"CA4194", "Air China", "Beijing", "Beijing Capital (PEK)", "Chengdu", "Chengdu Tianfu (TFU)", "07:30", "10:00", "150"},
            {"CA4195", "Air China", "Beijing", "Beijing Capital (PEK)", "Chengdu", "Chengdu Tianfu (TFU)", "11:00", "13:30", "150"},
            {"MU5301", "China Eastern", "Shanghai", "Shanghai Pudong (PVG)", "Guangzhou", "Guangzhou Baiyun (CAN)", "14:00", "16:10", "130"},
            {"MU5302", "China Eastern", "Guangzhou", "Guangzhou Baiyun (CAN)", "Shanghai", "Shanghai Pudong (PVG)", "17:00", "19:10", "130"},
        };

        BigDecimal[] basePrices = {
            new BigDecimal("800"), new BigDecimal("800"), new BigDecimal("1200"), new BigDecimal("1200"),
            new BigDecimal("600"), new BigDecimal("600"), new BigDecimal("900"), new BigDecimal("900"),
            new BigDecimal("500"), new BigDecimal("500")
        };

        List<Flight> flights = new ArrayList<>();
        for (int day = 0; day < 7; day++) {
            LocalDate date = LocalDate.now().plusDays(day + 1);
            for (int i = 0; i < routes.length; i++) {
                String[] r = routes[i];
                Flight flight = new Flight();
                flight.setFlightNo(r[0]);
                flight.setAirline(r[1]);
                flight.setDepartureCity(r[2]);
                flight.setDepartureAirport(r[3]);
                flight.setArrivalCity(r[4]);
                flight.setArrivalAirport(r[5]);
                flight.setDepartureTime(LocalTime.parse(r[6]));
                flight.setArrivalTime(LocalTime.parse(r[7]));
                flight.setDurationMinutes(Integer.parseInt(r[8]));
                flight.setFlightDate(date);
                flight.setBasePrice(basePrices[i]);
                flight.setStatus(FlightStatus.SCHEDULED);

                FlightSeat economy = new FlightSeat();
                economy.setFlight(flight);
                economy.setSeatClass(SeatClass.ECONOMY);
                economy.setSeatCount(150);
                economy.setBookedCount(0);
                economy.setPrice(basePrices[i]);

                FlightSeat business = new FlightSeat();
                business.setFlight(flight);
                business.setSeatClass(SeatClass.BUSINESS);
                business.setSeatCount(20);
                business.setBookedCount(0);
                business.setPrice(basePrices[i].multiply(new BigDecimal("2.5")));

                List<FlightSeat> seatList = new ArrayList<>();
                seatList.add(economy);
                seatList.add(business);
                flight.setSeats(seatList);

                flights.add(flight);
            }
        }
        flightRepository.saveAll(flights);
        log.info("Seeded {} flights.", flights.size());
        log.info("Demo data seeding complete.");
    }
}
