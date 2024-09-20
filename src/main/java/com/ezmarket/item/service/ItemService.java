package com.ezmarket.item.service;

import com.ezmarket.image.repository.ImageRepository;
import com.ezmarket.image.service.ImageService;
import com.ezmarket.item.domain.entity.Item;
import com.ezmarket.item.domain.enums.SellStatus;
import com.ezmarket.item.dto.ItemDto;
import com.ezmarket.item.repository.ItemRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ImageService imageService;
    private final ItemRepository itemRepository;
    private final ImageRepository imageRepository;

    @Transactional(readOnly = true)
    public List<ItemDto> getSellingItems(){
        List<Item> itemList = itemRepository.findBySellStatus(SellStatus.SELL);
        List<ItemDto> itemDtoList = itemList.stream().map(item-> ItemDto.ofEntity(item)).collect(Collectors.toList());
        return itemDtoList;
    }



    @Transactional(readOnly = true)
    public List<ItemDto> getAllItems(){
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


    @Transactional
    public void updateItem(ItemDto itemDto, ArrayList<MultipartFile> files) throws Exception {
        Item item = itemRepository.findById(itemDto.getId()).orElseThrow(EntityExistsException::new);
        item.updateItem(itemDto);

        boolean existFile = files.stream().anyMatch(file-> !file.isEmpty());
        if(existFile){
            imageService.updateImage(item, files);
        }

    }

    @Transactional
    public void deleteItem(Long id) throws Exception {
        Item item = itemRepository.findById(id).orElseThrow(EntityExistsException::new);

        // S3 삭제
        imageService.deleteImage(item);


        // DB 삭제
        itemRepository.delete(item);
    }

}
