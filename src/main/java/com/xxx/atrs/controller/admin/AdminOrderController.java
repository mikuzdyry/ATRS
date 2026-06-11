package com.xxx.atrs.controller.admin;

import com.xxx.atrs.entity.BookingOrder;
import com.xxx.atrs.repository.BookingOrderRepository;
import com.xxx.atrs.service.BookingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/order")
public class AdminOrderController {

    private final BookingService bookingService;
    private final BookingOrderRepository orderRepository;

    public AdminOrderController(BookingService bookingService, BookingOrderRepository orderRepository) {
        this.bookingService = bookingService;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<BookingOrder> orderPage = bookingService.listAll(PageRequest.of(page, 20));
        model.addAttribute("orderPage", orderPage);
        return "admin/order/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        BookingOrder order = orderRepository.findById(id).orElse(null);
        model.addAttribute("order", order);
        return "admin/order/detail";
    }

    @PostMapping("/cancel")
    public String cancel(@RequestParam Long orderId, RedirectAttributes redirectAttributes) {
        try {
            bookingService.adminCancel(orderId);
            redirectAttributes.addFlashAttribute("success", "订单已取消");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/order/list";
    }
}
