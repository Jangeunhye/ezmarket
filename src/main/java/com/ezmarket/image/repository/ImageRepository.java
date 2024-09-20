package com.ezmarket.image.repository;

import com.ezmarket.image.domain.entity.Image;
import com.ezmarket.item.domain.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByItem(Item item);
}
