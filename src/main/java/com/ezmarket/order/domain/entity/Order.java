package com.ezmarket.order.domain.entity;

import com.ezmarket.global.common.BaseTimeEntity;
import com.ezmarket.image.domain.entity.Image;
import com.ezmarket.member.domain.entity.Member;
import com.ezmarket.order.domain.enums.OrderStatus;
import com.ezmarket.orderItem.OrderItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="orders")
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private String receiverName;

    private String receiverPhone;

    private String receiverAddress;

    private Long totalAmounts;


    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItemList;


    public void addOrderItem(OrderItem orderItem){
        this.orderItemList.add(orderItem);
    }

    public void updateTotalAmounts(Long totalAmounts){
        this.totalAmounts = totalAmounts;
    }
}
