package com.ezmarket.item.repository;

import com.ezmarket.item.domain.entity.Item;
import com.ezmarket.item.domain.enums.SellStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findBySellStatus(SellStatus sellStatus);
}
