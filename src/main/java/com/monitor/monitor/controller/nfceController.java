package com.monitor.monitor.controller;


import com.monitor.monitor.api.ApiZabbix;
import com.monitor.monitor.api.records.TempoMedio;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<Map<String, Object>> linhas = new ArrayList<>();

        /*DATA NFCE*/
        List<String> listaData = new ArrayList<>();
        List<String> listaHoras = new ArrayList<>();
        List<String> listaTotal = new ArrayList<>();

        for (TempoMedio item : temponfce) {
            String data = item.value(); // Exemplo: "2024-05-13T10:00:00Z"

            String dataInt = data.length() > 9 ? data.substring(0, 10) : data;

            // Converte para LocalDate
            LocalDate dataConvertida = LocalDate.parse(dataInt); // formato ISO: yyyy-MM-dd

            // Formata como dd/MM/yyyy
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dataFormatada = dataConvertida.format(formatter);
            listaData.add(dataFormatada);

            /*HORA NFCE*/
            String hora = item.value();
            String horas = hora.length() > 11
                    ? hora.substring(11,20)
                    : hora;
            listaHoras.add(horas);

            String mediaTotal = item.value();
            String media = mediaTotal.length() > 2
                    ? mediaTotal.substring(mediaTotal.length() - 2)
                    : mediaTotal;

            String totalNf = item.value();
            String totalNfs = totalNf.length() > 2
                    ? totalNf.substring(28, 33)
                    : totalNf;

        Map<String, Object> linha = new HashMap<>();
            linha.put("data", dataFormatada);
            linha.put("hora", horas);
            linha.put("total", totalNfs);
            linha.put("media", media);
            linhas.add(linha);
        }

        /*TOTAL DE NFCE*/
        TempoMedio totalAPI = temponfce.get(0);
        String totalValor = totalAPI.value();
        int inicio = totalValor.indexOf("TOTAL:") + "TOTAL:".length();

        int fim = totalValor.indexOf("|", inicio);

        String total = totalValor.substring(inicio, fim).trim();


        /*ULTIMA MEDIA NFCE*/
        TempoMedio mediaAtual = temponfce.get(0);

        String mediaValor = mediaAtual.value();

        String media = mediaValor.substring(mediaValor.length()-2);




        /*ATRIBUTOS AO HTML*/
        model.addAttribute("media",media);
        model.addAttribute("linhas",linhas);
        model.addAttribute("total",total);

        return "nfce";
    }
}
