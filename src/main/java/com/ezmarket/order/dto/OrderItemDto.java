package com.ezmarket.order.dto;

import com.ezmarket.item.domain.entity.Item;
import com.ezmarket.orderItem.OrderItem;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class OrderItemDto {
    private Long itemId;
    private Integer quantity;
    private String itemName;
    private Long itemPrice;
    private String thumbnailImageUrl;
    private Long amounts;

    public static OrderItemDto ofEntity(Item item,Integer quantity){
        return OrderItemDto.builder()
                .itemId(item.getId())
                .itemName(item.getItemName())
                .itemPrice(item.getPrice())
                .quantity(quantity)
                .amounts(item.getPrice()*quantity)
                .thumbnailImageUrl(item.getImageList().get(0).getImageUrl())
                .build();
    }

    public OrderItem toEntity(Item item){
        OrderItem orderItem =  OrderItem.builder()
                .item(item)
                .quantity(this.quantity)
                .amounts(this.amounts)
                .build();

        return orderItem;
    }


}
