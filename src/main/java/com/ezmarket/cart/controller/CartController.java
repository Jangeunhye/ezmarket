package com.ezmarket.cart.controller;


import com.ezmarket.cart.dto.CartDto;
import com.ezmarket.cart.dto.CartItemRequestDto;
import com.ezmarket.cart.exception.EmptyCartException;
import com.ezmarket.cart.service.CartService;
import com.ezmarket.order.dto.OrderDto;
import com.ezmarket.order.exception.OutOfStockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CartController {


    private final CartService cartService;


    @PostMapping("/cart/add")
    public String addCartItem(@RequestParam("itemId") Long itemId, @RequestParam("quantity") Integer quantity,
                              Principal principal, RedirectAttributes redirectAttributes,
                              Model model){
        try{
            cartService.addCartItem(itemId, quantity,principal.getName());
            redirectAttributes.addFlashAttribute("successMessage","장바구니 담기 성공!");
        } catch (Exception e){
            model.addAttribute("errorMessage","장바구니 담기 실패");
        }

        return "redirect:/";
    }

    @GetMapping("/cart")
    public String getCart( Model model, Principal principal){
        CartDto cartDto = cartService.getCart(principal.getName());
        model.addAttribute("cartDto",cartDto);
        return "cart/cartList";
    }

    @PatchMapping("/cart/{cartItemId}")
    public ResponseEntity<Object> updateCartItem(@RequestBody CartItemRequestDto cartItemRequestDto, @PathVariable("cartItemId") Long cartItemId, Principal principal
    ,Model model){
        try{
            cartService.updateCartItem(cartItemRequestDto,cartItemId, principal.getName());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/cart/{cartItemId}")
    public ResponseEntity<Object> deleteCartItem(@PathVariable("cartItemId") Long cartItemId, Principal principal){
        try{
            cartService.deleteCartItem(cartItemId, principal.getName());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/cart/order")
    public String cartOrderForm(Model model, Principal principal,RedirectAttributes redirectAttributes){
        try{
            OrderDto orderDto = cartService.cartOrderForm(principal.getName());
            model.addAttribute("orderDto",orderDto);
        } catch (EmptyCartException e){
            redirectAttributes.addFlashAttribute("errorMessage",e.getMessage());
            return "redirect:/cart";
        }

        return "cart/cartOrderForm";
    }

    @PostMapping("/cart/order")
    public String cartOrder(OrderDto orderDto, Principal principal, RedirectAttributes redirectAttributes,Model model){
        try{
            cartService.cartOrder(orderDto, principal.getName());

        } catch(OutOfStockException e){
            model.addAttribute("errorMessage",e.getMessage());
            return "order/orderForm";
        }
        redirectAttributes.addFlashAttribute("successMessage","주문이 완료되었습니다.");

        return "redirect:/";
    }

}
