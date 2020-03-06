package com.cx.kpi2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/")
    public String firstPage(){
        return "redirect:login";
    }

    @GetMapping("/login")
    public String toLogin(){
        return "publicPage/login";
    }

    @GetMapping("/index")
    public String tohome(){
        return "publicPage/index";
    }
}
