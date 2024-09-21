package com.ezmarket.order.controller;

import com.ezmarket.item.domain.entity.Item;
import com.ezmarket.item.repository.ItemRepository;
import com.ezmarket.member.domain.entity.Member;
import com.ezmarket.member.dto.MemberDto;
import com.ezmarket.member.repository.MemberRepository;
import com.ezmarket.order.dto.OrderItemDto;
import com.ezmarket.order.service.OrderService;
import jakarta.persistence.EntityExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

        OrderItemDto orderItemDto = orderService.getOrderItem(itemId,quantity);
        model.addAttribute("orderItemDto",orderItemDto);

        MemberDto memberDto = orderService.getMemberDto(principal.getName());
        model.addAttribute("memberDto",memberDto);

        return "order/orderForm";
    }
}
