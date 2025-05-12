package com.monitor.monitor.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/nfce")
public class nfceController {

    @GetMapping
    public String nfce() {
        return "nfce";
    }
}
