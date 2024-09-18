package com.ezmarket.item.dto;

import com.ezmarket.image.domain.entity.Image;
import com.ezmarket.image.dto.ImageDto;
import com.ezmarket.item.domain.entity.Item;
import com.ezmarket.item.domain.enums.SellStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter @Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    private Long id;

    @NotNull(message = "판매 상태를 입력해주세요")
    private SellStatus sellStatus;

    @NotBlank(message = "상품 이름을 입력해주세요")
    private String itemName;

    @NotNull(message = "상품 가격을 입력해주세요")
    private Long itemPrice;

    @NotBlank(message = "상세 설명을 입력해주세요")
    private String itemDetail;

    @NotNull(message = "재고 수량을 입력해주세요")
    private Integer stock;

    private List<String> imageDtoList;

    public Item toEntity(){
        return Item.builder()
                .itemName(this.itemName)
                .itemDetail(this.itemDetail)
                .price(this.itemPrice)
                .stock(this.stock)
                .sellStatus(this.sellStatus)
                .imageList(new ArrayList<>())
                .build();
    }

    public static ItemDto ofEntity(Item item){
        return ItemDto.builder()
                .id(item.getId())
                .itemName(item.getItemName())
                .itemDetail(item.getItemDetail())
                .itemPrice(item.getPrice())
                .stock(item.getStock())
                .sellStatus(item.getSellStatus())
                .imageDtoList(item.getImageList().stream().map(image-> image.getImageUrl()).collect(Collectors.toList()))
                .build();
    }

}
