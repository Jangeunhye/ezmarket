package com.ezmarket.order.controller;

import com.ezmarket.item.repository.ItemRepository;
import com.ezmarket.member.repository.MemberRepository;
import com.ezmarket.order.OutOfStockException;
import com.ezmarket.order.dto.OrderDto;
import com.ezmarket.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

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
}
