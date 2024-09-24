package com.ezmarket.cart.dto;

import com.ezmarket.cart.domain.entity.Cart;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter @Setter
public class CartDto {

    private Long totalAmounts;
    private List<CartItemDto> cartItemDtoList;

    public static CartDto ofEntity(Cart cart, List<CartItemDto> cartItemDtoList){
        return CartDto.builder()
                .totalAmounts(cart.getTotalAmounts())
                .cartItemDtoList(cartItemDtoList)
                .build();
    }
}
