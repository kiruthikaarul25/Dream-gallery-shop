package com.shop.controller;
import com.shop.model.*;
import com.shop.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
@Controller
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private PackageService packageService;
    @Autowired
    private OwnerService ownerService;
    // ─── All Orders (Owner) ─────────────────────────
    @GetMapping
    public String listOrders(Authentication auth, Model model) {
        Owner owner = ownerService.getLoggedInOwner(auth.getName());
        model.addAttribute("orders",
            orderService.getAllOrdersByOwner(owner.getId()));
        model.addAttribute("revenue",
            orderService.getTotalRevenue(owner.getId()));
        model.addAttribute("owner", owner);
        return "owner/orders";
    }
    // ─── Create Order (Client) ──────────────────────
    @PostMapping("/create")
    public String createOrder(
            @RequestParam("packageId") int packageId,
            HttpSession session) {
        Integer clientId = (Integer) session.getAttribute("clientId");
        if (clientId == null)
            return "redirect:/login";
        Client client = clientService.getById(clientId).orElseThrow();
        packageService.getById(packageId).ifPresent(pkg -> {
            Order order = new Order();
            order.setClient(client);
            order.setPackageEntity(pkg);
            order.setAmount(pkg.getPrice());
            order.setStatus("pending");
            orderService.createOrder(order);
        });
        clientService.updateStatus(clientId, "paid");
        return "redirect:/gallery/view";
    }
    // ─── Mark as Paid (Owner) ───────────────────────
    @PostMapping("/{id}/paid")
    public String markPaid(
            @PathVariable("id") int id,
            @RequestParam("paymentMode") String paymentMode) {
        orderService.markAsPaid(id, paymentMode);
        return "redirect:/orders";
    }
    // ─── Cancel Order ───────────────────────────────
    @PostMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable("id") int id) {
        orderService.cancelOrder(id);
        return "redirect:/orders";
    }
    // ─── Order Detail ───────────────────────────────
    @GetMapping("/{id}")
    public String orderDetail(
            @PathVariable("id") int id,
            Authentication auth, Model model) {
        Owner owner = ownerService.getLoggedInOwner(auth.getName());
        orderService.getById(id).ifPresent(order ->
            model.addAttribute("order", order));
        model.addAttribute("owner", owner);
        return "owner/order-detail";
    }
}