package com.ezmarket.cart.dto;

import com.ezmarket.cart.domain.entity.CartItem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
public class CartItemDto {

    private Long cartItemId;

    public Long itemId;

    public Integer quantity;

    public Long amounts;

    public Long itemPrice;

    private String thumbnailImageUrl;

    private String itemName;

    public static CartItemDto ofEntity(CartItem cartItem){
        return CartItemDto.builder()
                .cartItemId(cartItem.getId())
                .amounts(cartItem.getAmounts())
                .quantity(cartItem.getQuantity())
                .itemId(cartItem.getItem().getId())
                .thumbnailImageUrl(cartItem.getItem().getImageList().get(0).getImageUrl())
                .itemPrice(cartItem.getItem().getPrice())
                .itemName(cartItem.getItem().getItemName())
                .build();
    }
}
