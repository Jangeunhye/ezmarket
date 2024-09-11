package com.ezmarket.item.domain.entity;

import com.ezmarket.global.common.BaseTimeEntity;
import com.ezmarket.image.domain.entity.Image;
import com.ezmarket.item.domain.enums.SellStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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


}
