package com.ezmarket.item.service;

import com.ezmarket.image.repository.ImageRepository;
import com.ezmarket.image.service.ImageService;
import com.ezmarket.item.domain.entity.Item;
import com.ezmarket.item.dto.ItemDto;
import com.ezmarket.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ImageService imageService;
    private final ItemRepository itemRepository;

    @Transactional(readOnly = true)
    public List<ItemDto> getItems(){
        List<Item> itemList = itemRepository.findAll();
        List<ItemDto> itemDtoList = itemList.stream().map(item-> ItemDto.ofEntity(item)).collect(Collectors.toList());
        return itemDtoList;
    }


    @Transactional
    public void createItem(ItemDto itemDto, ArrayList<MultipartFile> files) throws Exception {
        Item item = itemDto.toEntity();
        imageService.saveImage(item, files);
        itemRepository.save(item);
    }
    @Transactional(readOnly = true)
    public ItemDto getItemDetail(Long id){
        Item item = itemRepository.findById(id).orElseThrow(null);
        return ItemDto.ofEntity(item);

    }



}
