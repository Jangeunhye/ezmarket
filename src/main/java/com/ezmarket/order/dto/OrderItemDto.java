package com.ezmarket.order.dto;

import com.ezmarket.item.domain.entity.Item;
import com.ezmarket.item.dto.ItemDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@Builder
public class OrderItemDto {
    private Long itemId;
    private Integer quantity;
    private String itemName;
    private Long itemPrice;
    private String thumbnailImageUrl;

    public static OrderItemDto ofEntity(Item item,Integer quantity){
        return OrderItemDto.builder()
                .itemId(item.getId())
                .itemName(item.getItemName())
                .itemPrice(item.getPrice())
                .quantity(quantity)
                .build();
    }
}
