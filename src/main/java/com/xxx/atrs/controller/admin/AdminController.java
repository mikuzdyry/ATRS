package com.xxx.atrs.controller.admin;

import com.xxx.atrs.repository.BookingOrderRepository;
import com.xxx.atrs.repository.FlightRepository;
import com.xxx.atrs.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final FlightRepository flightRepository;
    private final BookingOrderRepository orderRepository;
    private final UserRepository userRepository;

    public AdminController(FlightRepository flightRepository,
                           BookingOrderRepository orderRepository,
                           UserRepository userRepository) {
        this.flightRepository = flightRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("flightCount", flightRepository.count());
        model.addAttribute("orderCount", orderRepository.count());
        model.addAttribute("paidOrderCount", orderRepository.countByStatus("PAID"));
        model.addAttribute("userCount", userRepository.count());
        return "admin/index";
    }
}
