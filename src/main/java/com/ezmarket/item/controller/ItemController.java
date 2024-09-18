package com.ezmarket.item.controller;


import com.ezmarket.item.domain.entity.Item;
import com.ezmarket.item.dto.ItemDto;
import com.ezmarket.item.service.ItemService;
import com.ezmarket.member.dto.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;



@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;


    @GetMapping("/")
    public String items(Model model){

        List<ItemDto> itemDtoList = itemService.getItems();
        log.info(itemDtoList.toString());
        model.addAttribute("itemDtoList",itemDtoList);
        return "main";
    }


    @GetMapping("/admin/item/new")
    public String createItem(Model model){
        model.addAttribute("itemDto",new ItemDto());
        return "item/itemForm";

    }

    @Transactional
    @PostMapping("/admin/item/new")
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
