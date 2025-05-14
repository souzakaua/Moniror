package com.monitor.monitor.controller;


import com.monitor.monitor.api.ApiZabbix;
import com.monitor.monitor.records.EmissByHr;
import com.monitor.monitor.records.TempoMedio;
import com.monitor.monitor.records.TotalNf;
import com.monitor.monitor.records.TotalNfDia;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
        List<TotalNf> totalnfce = apiZabbix.buscarTotalNf();
        List<EmissByHr> emissByHr = apiZabbix.buscarEmissByHr();
        List<TotalNfDia> totalNfDia = apiZabbix.buscarTotalNfDia();
        List<Map<String, Object>> linhas = new ArrayList<>();

        /*DATA NFCE*/
        List<String> listaData = new ArrayList<>();
        List<String> listaHoras = new ArrayList<>();

        for (TempoMedio item : temponfce) {
            String[] data = item.value().split(" ");
            String dataInt = data[0];
            String horas = data[1];
            String totalNf = data[4];
            String media = data[7];

            // Converte para LocalDate
            LocalDate dataConvertida = LocalDate.parse(dataInt); // formato ISO: yyyy-MM-dd

            // Formata como dd/MM/yyyy
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dataFormatada = dataConvertida.format(formatter);

        Map<String, Object> linha = new HashMap<>();
            linha.put("data", dataFormatada);
            linha.put("hora", horas);
            linha.put("total", totalNf);
            linha.put("media", media);
            linhas.add(linha);
        }

        //DECLARAÇÃO DAS VARIAVEIS
        String  tempoMedioAnt,
                horaEmiss,
                totalNotasDiario,
                totalNotasHr,
                totalNotasAnte,
                totalNotasDiaAtual,
                totalNotasDiaAnte,
                tempoMedio = "0";

        //RESGATANDO DA API O TOTAL DE NOTAS EMITIDAS NO DIA LINHA 1 E 2
        TotalNfDia totalNfByDiaAtual = totalNfDia.get(0);
        TotalNfDia totalNfByDiaAnte = totalNfDia.get(1);

        //TRATAMENTO DAS LINHAS DE EMISSÃO TOTAL DO DIA VINDAS DA API
        String[] nfByDiaAtual = totalNfByDiaAtual.value().split(" ");
        String[] nfByDiaAnte = totalNfByDiaAnte.value().split(" ");
        totalNotasDiaAtual = nfByDiaAtual[0];
        totalNotasDiaAnte = nfByDiaAnte[0];

        //RESGATANDO DA API O TOTAL DE NOTAS EMITIDAS NA ULTIMA HORA LINHA 1 E 2
        EmissByHr emissByHora = emissByHr.get(0);
        EmissByHr emissByHoraAnte = emissByHr.get(1);

        //TRATAMENTO DAS LINHAS DE EMISSÃO VINDAS DA API
        String[] emissao = emissByHora.value().split(" ");
        String [] emissaoAnte = emissByHoraAnte.value().split(" ");
        horaEmiss = emissao[2];

        //RESGATANDO DA API O TEMPO MEDIO LINHAS 1 E 2
        TempoMedio tempoMediaAtual = temponfce.get(0);
        TempoMedio tempoMediaAnte = temponfce.size() > 1 ? temponfce.get(1) : new TempoMedio("valor vazio");

        //TRATAMENTO DAS LINHAS DE TEMPO MEDIO VINDAS DA API
        String[] tempoMedioAtual = tempoMediaAtual.value().split(" ");
        String[] tempoMedioAnte = tempoMediaAnte.value().split(" ");
        totalNotasHr = tempoMedioAtual[4];
        totalNotasAnte = tempoMedioAnte[4];
        tempoMedio = tempoMedioAtual[7];
        tempoMedioAnt = tempoMedioAnte[7];
        totalNotasDiario = tempoMedioAtual[4];

        String simboloMedia = "0"; String  simboloNotasDia = "0"; String simboloNotas = "0";

        // CONVERSÃO DO TEMPO MEDIO DE "STRING" PARA "INTEGER"
        int mediaAnteInt = Integer.parseInt(tempoMedioAnt);
        int mediaAtualInt = Integer.parseInt(tempoMedio);

        //CALCULO DA VARIAÇÃO DE TEMPO MEDIO (TEMPO MAIOR OU MENOR)
        int diferencaMedia = Math.abs(mediaAtualInt - mediaAnteInt);
        if (mediaAnteInt < mediaAtualInt) {
            simboloMedia = "↑";
        }if (mediaAnteInt > mediaAtualInt) {
            simboloMedia = "↓";
        }if (mediaAnteInt == mediaAtualInt) {
            simboloMedia = "-";
        }

        // CONVERSÃO DO TOTAL DE NOTAS POR HORA DE "STRING" PARA "INTEGER"
        int notasAnteInt = Integer.parseInt(totalNotasAnte);
        int notasAtualInt = Integer.parseInt(totalNotasHr);

        //CALCULO DA VARIAÇÃO DE NOTAS (MAIOR OU MENOR)
        int diferencaNotas = Math.abs(notasAtualInt - notasAnteInt);
        if (notasAnteInt < notasAtualInt) {
            simboloNotas = "↑";
        }if (notasAnteInt > notasAtualInt) {
            simboloNotas = "↓";
        }if (notasAnteInt == notasAtualInt) {
            simboloNotas = "-";
        }

        // CONVERSÃO DO TOTAL DE NOTAS POR DIA DE "STRING" PARA "INTEGER"
        int notasDiaAnteInt = Integer.parseInt(totalNotasDiaAnte);
        int notasDiaAtualInt = Integer.parseInt(totalNotasDiaAtual);

        //CALCULO DA VARIAÇÃO DE NOTAS DIA (MAIOR OU MENOR)
        int diferencaNotasDia = Math.abs(notasDiaAnteInt - notasDiaAtualInt);
        if (notasDiaAnteInt < notasDiaAtualInt) {
            simboloNotasDia = "↑";
        }if (notasDiaAnteInt > notasDiaAtualInt) {
            simboloNotasDia = "↓";
        }if (notasDiaAnteInt == notasDiaAtualInt) {
            simboloNotasDia = "-";
        }

        /*ATRIBUTOS AO HTML*/
        model.addAttribute("totalNotasDiaAtual",totalNotasDiaAtual);
        model.addAttribute("totalNotasDiaAnte",totalNotasDiaAnte);
        model.addAttribute("simboloNotasDia",simboloNotasDia);
        model.addAttribute("diferencaNotasDia",diferencaNotasDia);
        model.addAttribute("emiss",totalNotasHr);
        model.addAttribute("emissAnte",totalNotasAnte);
        model.addAttribute("simboloNotas",simboloNotas);
        model.addAttribute("diferencaNotas",diferencaNotas);
        model.addAttribute("media",tempoMedio);
        model.addAttribute("tempMediaAnte",tempoMedioAnt);
        model.addAttribute("simboloMedia", simboloMedia);
        model.addAttribute("diferencaMedia", diferencaMedia);
        model.addAttribute("linhas",linhas);

        return "nfce";
    }

}

