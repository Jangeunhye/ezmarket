package com.ezmarket.cart.domain.entity;

import com.ezmarket.global.common.BaseTimeEntity;
import com.ezmarket.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder @Getter
public class Cart extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cart_id")
    private Long id;

    private Long totalAmounts;

    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "cart_item_id")
    private List<CartItem> cartItemList;

    @OneToOne
    @JoinColumn(name = "member_id",unique = true)
    private Member member;

    public void addCartItem(CartItem cartItem){
        this.cartItemList.add(cartItem);
        this.totalAmounts = this.totalAmounts + cartItem.getAmounts();
    }

    public void updateTotalAmounts(Long previousAmounts, Long newAmounts){
        this.totalAmounts = this.totalAmounts - previousAmounts + newAmounts;
    }

    public void updateTotalAmounts(Long deleteAmounts){
        this.totalAmounts = this.totalAmounts - deleteAmounts;
    }


}
