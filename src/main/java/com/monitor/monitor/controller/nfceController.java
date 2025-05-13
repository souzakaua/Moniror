package com.monitor.monitor.controller;


import com.monitor.monitor.api.ApiZabbix;
import com.monitor.monitor.api.records.TempoMedio;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/nfce")
public class nfceController {

    private final ApiZabbix apiZabbix;

    public nfceController(ApiZabbix apiZabbix) {
        this.apiZabbix = apiZabbix;
    }

    @GetMapping
    public String nfce(Model model) {
        List<TempoMedio> temponfce = apiZabbix.buscarTempoMedio();

        String dados = temponfce.get(0).value();

        System.out.println("dados no controller" + dados);
        return "nfce";
    }
}
