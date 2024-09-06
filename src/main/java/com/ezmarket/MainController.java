package com.ezmarket;

import com.ezmarket.member.dto.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {


    @GetMapping(value = "/")
    public String main(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        return "main";
    }
}
