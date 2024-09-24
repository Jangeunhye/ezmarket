package com.ezmarket.order.domain.entity;

import com.ezmarket.item.domain.entity.Item;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class OrderItem {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "order_item_id")
    private Long id;

    private Integer quantity;
    private Long amounts;

    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name="item_id")
    private Item item;

    public void linkOrder(Order order){
        this.order = order;

    }
}
