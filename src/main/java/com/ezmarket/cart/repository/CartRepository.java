package com.ezmarket.cart.repository;

import com.ezmarket.cart.domain.entity.Cart;
import com.ezmarket.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByMember(Member member);
}
