package com.ezmarket.item.controller;

import com.ezmarket.item.dto.ItemDto;
import com.ezmarket.item.service.ItemService;
import com.ezmarket.member.dto.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;



@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/")
    public String getSellingItems(Model model) {
        List<ItemDto> itemDtoList = itemService.getSellingItems();
        model.addAttribute("itemDtoList", itemDtoList);
        return "item/itemList";
    }

    @GetMapping("/admin/items")
    public String getAllItems(Model model) {
        List<ItemDto> itemDtoList = itemService.getAllItems();
        model.addAttribute("itemDtoList", itemDtoList);
        return "item/adminItemList";
    }

    @PutMapping("/admin/items/{id}")
    public String updateItem(@Valid ItemDto itemDto, BindingResult bindingResult,
                           @RequestPart(value = "uploadFile") ArrayList<MultipartFile> files) throws Exception{

        itemService.updateItem(itemDto,files);

        return "redirect:/items/"+itemDto.getId();
    }


    @DeleteMapping("/admin/items/{id}")
    public String deleteItem(@PathVariable("id") Long id) throws Exception {
        itemService.deleteItem(id);
        return "redirect:/";
    }




    @GetMapping("/admin/items/{id}/update")
    public String updateItemForm(@PathVariable("id") Long id, Model model){
        ItemDto itemDto = itemService.getItemDetail(id);
        model.addAttribute("itemDto",itemDto);
        return "item/itemUpdate";
    }


    @GetMapping("/items/{id}")
    public String getItemDetail(@PathVariable("id") Long id, Model model){
        ItemDto itemDto = itemService.getItemDetail(id);
        model.addAttribute("itemDto",itemDto);
        return "item/itemDetail";
    }


    @GetMapping("/admin/items/new")
    public String createItem(Model model){
        model.addAttribute("itemDto",new ItemDto());
        return "item/itemForm";

    }

    @PostMapping("/admin/items/new")
    public String itemSave(@Valid ItemDto itemDto, BindingResult bindingResult,
                           @RequestPart(value = "uploadFile") ArrayList<MultipartFile> files,
                           @AuthenticationPrincipal CustomUserDetails customUserDetails,
                           Model model) throws Exception {
        model.addAttribute("itemDto",itemDto);
        model.addAttribute("uploadFile",files);
        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }
        itemService.createItem(itemDto, files);
        return "redirect:/";
    }

}
