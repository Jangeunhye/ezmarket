package com.ezmarket.order.service;

import com.ezmarket.item.domain.entity.Item;
import com.ezmarket.item.repository.ItemRepository;
import com.ezmarket.member.domain.entity.Member;
import com.ezmarket.member.domain.enums.Role;
import com.ezmarket.member.repository.MemberRepository;
import com.ezmarket.order.domain.entity.Order;
import com.ezmarket.order.dto.OrderDto;
import com.ezmarket.order.dto.OrderItemDto;
import com.ezmarket.order.repository.OrderRepository;
import com.ezmarket.order.domain.entity.OrderItem;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;


    @Transactional(readOnly = true)
    public OrderDto orderForm(Long itemId, Integer quantity, String email){
        Member member = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        OrderItemDto orderItemDto = OrderItemDto.ofEntity(item,quantity);


        List<OrderItemDto> orderItemDtoList = new ArrayList<>();
        orderItemDtoList.add(orderItemDto);

        Long totalAmounts = orderItemDtoList.stream()
                .mapToLong(OrderItemDto::getAmounts)
                .sum();
        OrderDto orderDto = OrderDto.createOrderDto(member, orderItemDtoList,totalAmounts);
        return orderDto;
    }

    @Transactional
    public void order(OrderDto orderDto,String email){
        Member member = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        handleOrder(orderDto,member);

    }


    public List<Order> getOrders(String roles, String email){

        List<Order> orderList  = new ArrayList<>();

        // ADMIN이라면
        if(roles.contains("ROLE_ADMIN")){
            orderList  = orderRepository.findAll();
        } else if (roles.contains("ROLE_USER")) {
            Member member = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
            orderList = orderRepository.findAllByMember(member);
        }

        return orderList;

    }

    @Transactional
    public void updateOrderStatus(Long orderId,String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        if (!member.getId().equals(order.getMember().getId()) && !member.getRole().equals(Role.ROLE_ADMIN)) {
            throw new IllegalStateException("취소 권한이 없습니다.");
        }

        order.cancelOrder();
    }


    public void handleOrder(OrderDto orderDto,Member member){

        Order order = orderDto.toEntity(member);

        Long totalAmounts = 0L;

        for (OrderItemDto orderItemDto : orderDto.getOrderItemDtoList()){
            // 1. 아이템 있는지 확인하기
            Item item = itemRepository.findById(orderItemDto.getItemId()).orElseThrow(EntityNotFoundException::new);

            // 2. 재고수량 주문수량 확인 후 빼기
            item.removeStock(orderItemDto.getQuantity());

            // 3. 엔티티로 변환
            OrderItem orderItem = orderItemDto.toEntity(item);

            // 4. amounts
            totalAmounts += orderItemDto.getAmounts();

            // 5. 양방향 매칭
            order.addOrderItem(orderItem);
            orderItem.linkOrder(order);

        }

        // total_amounts 업데이트
        order.updateTotalAmounts(totalAmounts);

        // Order 저장
        orderRepository.save(order);
    }
}
