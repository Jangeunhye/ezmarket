package com.ezmarket.item.service;

import com.ezmarket.image.repository.ImageRepository;
import com.ezmarket.image.service.ImageService;
import com.ezmarket.image.service.S3Service;
import com.ezmarket.item.domain.entity.Item;
import com.ezmarket.item.dto.ItemDto;
import com.ezmarket.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ImageService imageService;
    private final ItemRepository itemRepository;
    private final ImageRepository imageRepository;

    public void createProduct(ItemDto itemDto, ArrayList<MultipartFile> files) throws Exception {


        Item item = itemDto.toEntity();
        imageService.saveImage(item, files);
        itemRepository.save(item);



    }



}
