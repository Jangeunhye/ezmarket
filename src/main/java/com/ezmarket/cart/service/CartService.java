package com.ezmarket.cart.service;

import com.ezmarket.cart.domain.entity.Cart;
import com.ezmarket.cart.dto.CartDto;
import com.ezmarket.cart.dto.CartItemDto;
import com.ezmarket.cart.dto.CartItemRequestDto;
import com.ezmarket.cart.exception.EmptyCartException;
import com.ezmarket.cart.repository.CartItemRepository;
import com.ezmarket.cart.repository.CartRepository;
import com.ezmarket.cart.domain.entity.CartItem;
import com.ezmarket.item.domain.entity.Item;
import com.ezmarket.item.repository.ItemRepository;
import com.ezmarket.member.domain.entity.Member;
import com.ezmarket.member.repository.MemberRepository;
import com.ezmarket.order.dto.OrderDto;
import com.ezmarket.order.dto.OrderItemDto;
import com.ezmarket.order.repository.OrderRepository;
import com.ezmarket.order.service.OrderService;
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
public class CartService {

    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    @Transactional(readOnly = true)
    public CartDto getCart(String email){
        Member member = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        Cart cart = cartRepository.findByMember(member);

        // 카트 없으면 임시로 빈 DTO 보내기
        if(cart==null){
            return CartDto.builder()
                    .cartItemDtoList(new ArrayList<>())
                    .build();
        }


        List<CartItemDto> cartItemDtoList = cart.getCartItemList().stream()
                .map(CartItemDto::ofEntity).toList();


        return CartDto.ofEntity(cart, cartItemDtoList);
    }


    @Transactional
    public void updateCartItem(CartItemRequestDto cartItemRequestDto, Long cartItemId, String email){
        Member member = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);

        Cart cart = cartRepository.findByMember(member);

        CartItem cartItem = cartItemRepository.findByCart(cart);

        Long previousAmounts = cartItem.getAmounts();
        cartItem.updateCartItem(cartItemRequestDto.getQuantity());
        cart.updateTotalAmounts(previousAmounts , cartItem.getAmounts());

    }

    @Transactional(readOnly = true)
    public OrderDto cartOrderForm(String email){
        Member member = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        Cart cart = cartRepository.findByMember(member);

        if(cart.getCartItemList().isEmpty()){
            throw new EmptyCartException("장바구니가 비었습니다.");
        }

        List<OrderItemDto> orderItemDtoList = new ArrayList<>();
        cart.getCartItemList().forEach(cartItem ->
        {
            OrderItemDto orderItemDto = OrderItemDto.createOrderItemDtoFromCartItem(cartItem);
            orderItemDtoList.add(orderItemDto);
        });

        return OrderDto.createOrderDto(member, orderItemDtoList, cart.getTotalAmounts());


    }

    @Transactional
    public void deleteCartItem(Long cartItemId,String email){
        Member member = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);

        Cart cart = cartRepository.findByMember(member);

        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

        if(!cartItem.getCart().getMember().equals(member)){
            throw new IllegalArgumentException("해당 장바구니 항목을 삭제할 권한이 없습니다.");
        }
        Long deleteAmounts = cartItem.getAmounts();

        cart.updateTotalAmounts(deleteAmounts);

        cartItemRepository.delete(cartItem);
    }

    @Transactional
    public void addCartItem(Long itemId, Integer quantity, String email){

        Member member = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);

        // Cart 있는지 확인하고, 없으면 생성
        Cart cart = getOrCreateCart(member);

        // 해당 Item 있는 지 확인 후, 없으면 에러 발생
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

        // 해당 Item 이 CartItem 에 들어있는지 확인
        CartItem cartItem = cart.getCartItemList().stream().filter((tempCartItem -> tempCartItem.getItem().getId().equals(item.getId())))
                .findFirst().orElse(null);

        if(cartItem!=null){
            Long previousAmounts = cartItem.getAmounts();
            cartItem.addExistingCartItem(quantity);
            cart.updateTotalAmounts(previousAmounts , cartItem.getAmounts());
        }
        else{
             cartItem = createNewCartItem(cart, item, quantity);
        }

        cart.addCartItem(cartItem);

    }




    @Transactional
    public void cartOrder(OrderDto orderDto,String email){

        Member member = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        orderService.handleOrder(orderDto,member);

        // 장바구니 비우기
        Cart cart = cartRepository.findByMember(member);
        cart.getCartItemList().clear();
        Long deleteAmounts = cart.getTotalAmounts();
        cart.updateTotalAmounts(deleteAmounts);

    }

    public Cart getOrCreateCart(Member member){

        // 카트가 존재하지 않으면 새로 생성
        Cart cart = cartRepository.findByMember(member);
        if(cart==null){
            cart = Cart.builder().member(member)
                    .cartItemList(new ArrayList<>())
                    .totalAmounts(0L)
                    .build();
            cartRepository.save(cart);
        }
        return cart;
    }


    public CartItem createNewCartItem(Cart cart, Item item, Integer quantity){
        Long amounts = quantity*item.getPrice();
        return CartItem.builder()
                .cart(cart)
                .item(item)
                .quantity(quantity)
                .amounts(amounts)
                .build();
    }


}
