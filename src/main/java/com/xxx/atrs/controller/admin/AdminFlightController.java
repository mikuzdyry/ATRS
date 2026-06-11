package com.xxx.atrs.controller.admin;

import com.xxx.atrs.common.constant.FlightStatus;
import com.xxx.atrs.common.constant.SeatClass;
import com.xxx.atrs.common.exception.BusinessException;
import com.xxx.atrs.entity.Flight;
import com.xxx.atrs.entity.FlightSeat;
import com.xxx.atrs.service.FlightService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/flight")
public class AdminFlightController {

    private final FlightService flightService;

    public AdminFlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("flights", flightService.listAll());
        return "admin/flight/list";
    }

    @GetMapping("/add")
    public String addPage(Model model) {
        model.addAttribute("flight", new Flight());
        return "admin/flight/form";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute Flight flight,
                      @RequestParam("seatClass") String[] seatClasses,
                      @RequestParam("seatCount") int[] seatCounts,
                      @RequestParam("seatPrice") BigDecimal[] seatPrices,
                      RedirectAttributes redirectAttributes) {
        try {
            List<FlightSeat> seats = new ArrayList<>();
            for (int i = 0; i < seatClasses.length; i++) {
                FlightSeat seat = new FlightSeat();
                seat.setFlight(flight);
                seat.setSeatClass(SeatClass.valueOf(seatClasses[i]));
                seat.setSeatCount(seatCounts[i]);
                seat.setBookedCount(0);
                seat.setPrice(seatPrices[i]);
                seats.add(seat);
            }
            flight.setSeats(seats);
            flight.setStatus(FlightStatus.SCHEDULED);
            flightService.save(flight);
            redirectAttributes.addFlashAttribute("success", "航班添加成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/flight/list";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Long id, Model model) {
        Flight flight = flightService.getDetailWithSeats(id);
        model.addAttribute("flight", flight);
        return "admin/flight/form";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @ModelAttribute Flight flight,
                       @RequestParam("seatClass") String[] seatClasses,
                       @RequestParam("seatCount") int[] seatCounts,
                       @RequestParam("seatPrice") BigDecimal[] seatPrices,
                       RedirectAttributes redirectAttributes) {
        try {
            Flight existing = flightService.getDetailWithSeats(id);
            java.util.Map<SeatClass, Integer> bookedMap = new java.util.HashMap<>();
            for (FlightSeat oldSeat : existing.getSeats()) {
                bookedMap.put(oldSeat.getSeatClass(), oldSeat.getBookedCount());
            }

            existing.setFlightNo(flight.getFlightNo());
            existing.setAirline(flight.getAirline());
            existing.setDepartureCity(flight.getDepartureCity());
            existing.setArrivalCity(flight.getArrivalCity());
            existing.setDepartureAirport(flight.getDepartureAirport());
            existing.setArrivalAirport(flight.getArrivalAirport());
            existing.setDepartureTime(flight.getDepartureTime());
            existing.setArrivalTime(flight.getArrivalTime());
            existing.setDurationMinutes(flight.getDurationMinutes());
            existing.setFlightDate(flight.getFlightDate());
            existing.setBasePrice(flight.getBasePrice());

            existing.getSeats().clear();
            for (int i = 0; i < seatClasses.length; i++) {
                SeatClass sc = SeatClass.valueOf(seatClasses[i]);
                int oldBooked = bookedMap.getOrDefault(sc, 0);
                if (seatCounts[i] < oldBooked) {
                    throw new BusinessException(sc.name() + "舱已售出" + oldBooked + "座，座位数不能低于已售数量");
                }
                FlightSeat seat = new FlightSeat();
                seat.setFlight(existing);
                seat.setSeatClass(sc);
                seat.setSeatCount(seatCounts[i]);
                seat.setBookedCount(oldBooked);
                seat.setPrice(seatPrices[i]);
                existing.getSeats().add(seat);
            }
            flightService.save(existing);
            redirectAttributes.addFlashAttribute("success", "航班更新成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/flight/list";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            flightService.delete(id);
            redirectAttributes.addFlashAttribute("success", "航班已删除");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/flight/list";
    }
}
