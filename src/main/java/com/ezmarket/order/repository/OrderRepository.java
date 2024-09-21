package com.ezmarket.order.repository;

import com.ezmarket.member.domain.entity.Member;
import com.ezmarket.order.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByMember(Member member);
}
