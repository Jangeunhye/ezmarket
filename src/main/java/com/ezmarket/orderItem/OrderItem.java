package com.ezmarket.orderItem;

import com.ezmarket.item.domain.entity.Item;
import com.ezmarket.order.domain.entity.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class OrderItem {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "order_item")
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
