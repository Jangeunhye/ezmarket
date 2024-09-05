package com.ezmarket.member.controller;

import com.ezmarket.member.dto.MemberDto;

import com.ezmarket.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/signup")
    public String signUpForm(Model model){
        model.addAttribute("memberDto", new MemberDto());
        return "member/signup";
    }

    @PostMapping("/signup")
    public String signUp(@Valid MemberDto memberDto, BindingResult bindingResult) {
        memberService.checkDuplicateEmail(memberDto.getEmail(), bindingResult);
        if (bindingResult.hasErrors()){
            return "member/signup";
        }

        memberService.signUp(memberDto);

        return "redirect:/members/login";
    }

    @GetMapping("/login")
    public String login(){
        return "member/login";
    }

}
