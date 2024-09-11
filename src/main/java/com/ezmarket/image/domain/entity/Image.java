package com.ezmarket.image.domain.entity;

import com.ezmarket.item.domain.entity.Item;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="image_id")
    private Long id;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    public void linkItem(Item item){
        this.item= item;
    }

}
