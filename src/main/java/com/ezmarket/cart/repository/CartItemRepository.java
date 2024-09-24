package com.ezmarket.cart.repository;

import com.ezmarket.cart.domain.entity.Cart;
import com.ezmarket.cart.domain.entity.CartItem;
import com.ezmarket.item.domain.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    CartItem findByCart(Cart cart);

    CartItem findByItem(Item item);
}
