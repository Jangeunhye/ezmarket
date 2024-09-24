package com.ezmarket.item.domain.entity;

import com.ezmarket.global.common.BaseTimeEntity;
import com.ezmarket.image.domain.entity.Image;
import com.ezmarket.item.domain.enums.SellStatus;
import com.ezmarket.item.dto.ItemDto;
import com.ezmarket.order.exception.OutOfStockException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Item extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String itemName;

    @Column(name = "item_price")
    private Long price;

    private String itemDetail;

    private Integer stock;

    @Enumerated(EnumType.STRING)
    private SellStatus sellStatus;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> imageList;



    public void addItemImages(Image image){
        this.imageList.add(image);
    }

    public void updateItem(ItemDto itemDto){
        this.itemName = itemDto.getItemName();
        this.price = itemDto.getItemPrice();
        this.itemDetail = itemDto.getItemDetail();
        this.stock = itemDto.getStock();
        this.sellStatus = itemDto.getSellStatus();
    }

    public void removeStock(int quantity){
        int restStock = this.stock - quantity;
        if(restStock<0){
            throw new OutOfStockException("상품의 재고가 부족합니다. (현재 재고 수량: "+ this.stock+")");
        }
        this.stock = restStock;
    }
}
