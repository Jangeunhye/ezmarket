package com.ezmarket.order.dto;

import com.ezmarket.member.domain.entity.Member;
import com.ezmarket.order.domain.entity.Order;
import com.ezmarket.order.domain.enums.OrderStatus;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
public class OrderDto {

    private String receiverName;
    private String receiverAddress;
    private String receiverPhone;
    private Long total_amounts;

    private List<OrderItemDto> orderItemDtoList;


    public static OrderDto createOrderDto(Member member, List<OrderItemDto> orderItemDtoList, Long totalAmounts){
        OrderDto orderDto = OrderDto.builder()
                .receiverName(member.getName())
                .receiverPhone(member.getPhone())
                .receiverAddress(member.getAddress())
                .total_amounts(totalAmounts)
                .orderItemDtoList(orderItemDtoList)
                .build();

        return orderDto;
    }

    public Order toEntity(Member member){
        Order order = Order.builder()
                .receiverName(this.receiverName)
                .receiverPhone(this.receiverPhone)
                .receiverAddress(this.receiverAddress)
                .orderItemList(new ArrayList<>())
                .totalAmounts(this.total_amounts)
                .orderStatus(OrderStatus.ORDER)
                .member(member)
                .build();
        return order;
    }
}
