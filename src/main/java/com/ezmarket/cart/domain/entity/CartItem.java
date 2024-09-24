package com.ezmarket.cart.domain.entity;

import com.ezmarket.item.domain.entity.Item;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder @Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    private Integer quantity;

    private Long amounts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public void addExistingCartItem(Integer quantity){
        this.quantity = this.quantity + quantity;

        // 금액 업데이트
        this.amounts = this.amounts+ quantity * item.getPrice();
    }

    public void updateCartItem(Integer quantity){

        this.quantity = quantity;
        this.amounts = quantity*item.getPrice();
    }


}
