package com.xxx.atrs.controller;

import com.xxx.atrs.dto.BookingRequestDTO;
import com.xxx.atrs.entity.BookingOrder;
import com.xxx.atrs.entity.Flight;
import com.xxx.atrs.entity.User;
import com.xxx.atrs.service.BookingService;
import com.xxx.atrs.service.FlightService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class BookingController {

    private final BookingService bookingService;
    private final FlightService flightService;

    public BookingController(BookingService bookingService, FlightService flightService) {
        this.bookingService = bookingService;
        this.flightService = flightService;
    }

    @GetMapping("/booking/select/{flightId}")
    public String selectPage(@PathVariable Long flightId, @RequestParam String seatClass, Model model) {
        Flight flight = flightService.getDetailWithSeats(flightId);
        model.addAttribute("flight", flight);
        model.addAttribute("seatClass", seatClass);
        return "booking/select";
    }

    @PostMapping("/booking/submit")
    public String submit(@Valid BookingRequestDTO dto, BindingResult bindingResult,
                         HttpSession session, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", bindingResult.getFieldError().getDefaultMessage());
            return "redirect:/booking/select/" + dto.getFlightId() + "?seatClass=" + dto.getSeatClass();
        }
        try {
            User user = (User) session.getAttribute("currentUser");
            BookingOrder order = bookingService.createOrder(user.getId(), dto);
            return "redirect:/booking/confirm/" + order.getOrderNo();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/booking/select/" + dto.getFlightId() + "?seatClass=" + dto.getSeatClass();
        }
    }

    @GetMapping("/booking/confirm/{orderNo}")
    public String confirmPage(@PathVariable String orderNo, Model model, HttpSession session) {
        BookingOrder order = bookingService.getByOrderNo(orderNo);
        User user = (User) session.getAttribute("currentUser");
        if (!order.getUser().getId().equals(user.getId())) {
            return "error";
        }
        model.addAttribute("order", order);
        return "booking/confirm";
    }

    @PostMapping("/booking/pay/{orderNo}")
    public String pay(@PathVariable String orderNo, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            User user = (User) session.getAttribute("currentUser");
            bookingService.pay(user.getId(), orderNo);
            redirectAttributes.addFlashAttribute("success", "支付成功！电子票已生成");
            return "redirect:/order/detail/" + orderNo;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/booking/confirm/" + orderNo;
        }
    }

    @GetMapping("/order/list")
    public String orderList(HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");
        List<BookingOrder> orders = bookingService.listByUser(user.getId());
        model.addAttribute("orders", orders);
        return "order/list";
    }

    @GetMapping("/order/detail/{orderNo}")
    public String orderDetail(@PathVariable String orderNo, Model model) {
        BookingOrder order = bookingService.getByOrderNo(orderNo);
        model.addAttribute("order", order);
        return "booking/detail";
    }

    @PostMapping("/order/cancel/{orderNo}")
    public String cancelOrder(@PathVariable String orderNo, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            User user = (User) session.getAttribute("currentUser");
            bookingService.cancel(user.getId(), orderNo);
            redirectAttributes.addFlashAttribute("success", "订单已取消");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/order/list";
    }
}
