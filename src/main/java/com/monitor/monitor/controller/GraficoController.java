package com.monitor.monitor.controller;

import com.monitor.monitor.api.ApiZabbix;
import com.monitor.monitor.records.TempoMedio;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class GraficoController {

    private final ApiZabbix apiZabbix;

    public GraficoController(ApiZabbix apiZabbix) {
        this.apiZabbix = apiZabbix;
    }

    @GetMapping("/grafico-tempo")
    public List<Map<String, Object>> graficoTempoMedio() {
        List<TempoMedio> tempos = apiZabbix.buscarTempoMedio();

        List<Map<String, Object>> resultado = new ArrayList<Map<String, Object>>();
        for (TempoMedio t : tempos) {
            try {
                String value = t.value(); // "2025-05-14 04:26:40 | TOTAL: 6 | MEDIA: 39"

                String[] partes = value.split("\\|");

                String horaCompleta = partes[0].trim().split(" ")[1]; // "04:26:40"
                String hora = horaCompleta.substring(0, 5); // "04"

                String mediaStr = partes[2].replace("MEDIA:", "").trim(); // "39"

                Map<String, Object> ponto = new HashMap<String, Object>();
                ponto.put("hora", hora);
                ponto.put("media", Integer.parseInt(mediaStr));

                resultado.add(ponto);
            } catch (Exception e) {
                System.out.println("Erro ao processar tempo médio: " + t.value());
            }
        }

        return resultado;
    }
}