package com.ezmarket.order.service;

import com.ezmarket.item.domain.entity.Item;
import com.ezmarket.item.repository.ItemRepository;
import com.ezmarket.member.domain.entity.Member;
import com.ezmarket.member.dto.MemberDto;
import com.ezmarket.member.repository.MemberRepository;
import com.ezmarket.order.dto.OrderItemDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public OrderItemDto getOrderItem(Long itemId, Integer quantity){
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        OrderItemDto orderItemDto = OrderItemDto.ofEntity(item,quantity);
        orderItemDto.setThumbnailImageUrl(item.getImageList().get(0).getImageUrl());
        return orderItemDto;
    }

    @Transactional(readOnly = true)
    public MemberDto getMemberDto(String email){
        Member member = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        MemberDto memberDto = MemberDto.ofEntity(member);
        return memberDto;
    }
}
