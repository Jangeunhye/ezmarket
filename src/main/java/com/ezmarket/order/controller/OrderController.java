package com.ezmarket.order.controller;

import com.ezmarket.item.repository.ItemRepository;
import com.ezmarket.member.dto.CustomUserDetails;
import com.ezmarket.member.repository.MemberRepository;
import com.ezmarket.order.exception.OutOfStockException;
import com.ezmarket.order.domain.entity.Order;
import com.ezmarket.order.dto.OrderDto;
import com.ezmarket.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class OrderController {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final OrderService orderService;

    public OrderController(MemberRepository memberRepository, ItemRepository itemRepository, OrderService orderService) {
        this.memberRepository = memberRepository;
        this.itemRepository = itemRepository;
        this.orderService = orderService;
    }

    @GetMapping("/order")
    public String orderForm(@RequestParam("itemId") Long itemId, @RequestParam("quantity") Integer quantity, Model model, Principal principal){

        OrderDto orderDto = orderService.orderForm(itemId,quantity,principal.getName());
        model.addAttribute("orderDto",orderDto);

        return "order/orderForm";
    }

    @PostMapping("/order")
    public String order(OrderDto orderDto, Principal principal, RedirectAttributes redirectAttributes,Model model){
        try{
            orderService.order(orderDto, principal.getName());

        } catch(OutOfStockException e){
            model.addAttribute("errorMessage",e.getMessage());
            return "order/orderForm";
        }
        redirectAttributes.addFlashAttribute("successMessage","주문이 완료되었습니다.");

        return "redirect:/";
    }

    @GetMapping("/orders")
    public String getOrders(@AuthenticationPrincipal CustomUserDetails customUserDetails,Model model){

        String roles = customUserDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        String email = customUserDetails.getUsername();

        List<Order> orderList = orderService.getOrders(roles,email);
        model.addAttribute("orderList",orderList);
        return "order/orders";
    }

    @PatchMapping("/order/{orderId}/cancel")
    public ResponseEntity<Object> cancelOrder(@PathVariable Long orderId, Principal principal){
        try{
            orderService.updateOrderStatus(orderId,principal.getName());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("주문 취소 실패");
        }
    }
}
