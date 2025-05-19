package com.monitor.monitor.controller;


import com.monitor.monitor.api.ApiZabbix;
import com.monitor.monitor.records.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/nfce")
public class NfceController {
    private final ApiZabbix apiZabbix;

    public NfceController(ApiZabbix apiZabbix) {
        this.apiZabbix = apiZabbix;
    }

    @GetMapping
    public String nfce(Model model) {
        List<TempoMedio> temponfce = apiZabbix.buscarTempoMedio();
        List<EmissByHr> emissByHr = apiZabbix.buscarEmissByHr();
        List<TesteApi> emissaoHoraTeste = apiZabbix.buscarTeste();
        List<TotalNfDia> totalDiario = apiZabbix.buscarTotalNfDia();
        List<BuscaRegional> dadosRegional = apiZabbix.BuscaRegional();
        List<Map<String, Object>> linhas = new ArrayList<>();

        /*TABELA 1 TEMPO MEDIO*/

        String temp = emissaoHoraTeste.get(0).value();

        String[] linhasTemp = temp.split("\n");

        List<Map<String, Object>> table1 = new ArrayList<>();

        for (String linha : linhasTemp) {

            linha = linha.trim();

            String[] partes = linha.split("\\|");

            if (partes.length == 3) {
                String dataTabela1 = partes[0].split(" ")[0].trim();
                String horaTabela1 = partes[0].split(" ")[1].trim();
                String totalTabela1 = partes[1].split(":")[1].trim();
                String mediaTabela1 = partes[2].split(":")[1].trim();

                LocalDate dataConvertida = LocalDate.parse(dataTabela1); // formato ISO: yyyy-MM-dd

                // Formata como dd/MM/yyyy
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String dataFormatada = dataConvertida.format(formatter);

//                System.out.println("dat:" +dataTabela1);
//                System.out.println("hor:" +horaTabela1);
//                System.out.println("total:" +totalTabela1);
//                System.out.println("media tabela: " +mediaTabela1);


                Map<String, Object> tabelaTempo = new HashMap<>();
                tabelaTempo.put("dataTabela1", dataFormatada);
                tabelaTempo.put("horaTabela1", horaTabela1);
                tabelaTempo.put("totalTabela1", totalTabela1);
                tabelaTempo.put("mediaTabela1", mediaTabela1);

                table1.add(tabelaTempo);

                model.addAttribute("table1", table1);
            }
        }

        /* **********BOX 1 (TOTAL DIARIO) ***********/

        /*TRAZENDO OS DADOS DA API*/
        TotalNfDia totalNf1 = totalDiario.get(0);
        String nfDia1 = totalNf1.value();

        TotalNfDia totalNf2 = totalDiario.get(1);
        String nfDia2 = totalNf2.value();

        /*CONVERSÃO DE DADOS PARA CALCULO DE DIFERENÇA*/
        double totalNF1 = Double.parseDouble(nfDia1);
        double totalNF2 = Double.parseDouble(nfDia2);
        double dif = totalNF1 - totalNF2;

        model.addAttribute("totalNF1", totalNF1);
        model.addAttribute("totalNF2", totalNF2);
        model.addAttribute("dif", dif);
        model.addAttribute("totalNotasDiaAtual",nfDia1);
        /* **********FIM BOX 1 (TOTAL DIARIO) ***********/

        /* **********BOX 2 (TOTAL QUANTIDADE DOC) ***********/

        TempoMedio totalDoc1 = temponfce.get(0);
        TempoMedio totalDoc2 = temponfce.size() > 1 ? temponfce.get(1) : new TempoMedio("valor vazio");

        String[] doc1 = totalDoc1.value().split(" ");
        String[] doc2 = totalDoc2.value().split(" ");

        String documentos1 = doc1[4];
        String documentos2 = doc2[4];

        int notasAnteInt = Integer.parseInt(documentos1);
        int notasAtualInt = Integer.parseInt(documentos2);

        int difDoc = Math.abs(notasAtualInt - notasAnteInt);

        model.addAttribute("documentos1",documentos1);
        model.addAttribute("documentos2",documentos2);
        model.addAttribute("difDoc",difDoc);
        /* **********FIM BOX 2 (QUANTIDADE DOC) ***********/

        /* **********BOX 3 (TEMPO MEDIO) ***********/

        TempoMedio tempoMedia1 = temponfce.get(0);
        TempoMedio tempoMedia2 = temponfce.size() > 1 ? temponfce.get(1) : new TempoMedio("valor vazio");

        String[] tempoMedioAtual1 = tempoMedia1.value().split(" ");
        String[] tempoMedioAnte2 = tempoMedia2.value().split(" ");

        String tempoMedio1 = tempoMedioAtual1[7];
        String tempoMedio2 = tempoMedioAnte2[7];

        int media1 = Integer.parseInt(tempoMedio1);
        int media2 = Integer.parseInt(tempoMedio2);

        int difMedia = Math.abs(media1 - media2);

        model.addAttribute("media1",media1);
        model.addAttribute("media2",media2);
        model.addAttribute("difMedia", difMedia);

        /* **********FIM BOX 3 (TEMPO MEDIO) ***********/

        /* **********BOX 4 (EMISSAO HORA) ***********/
        EmissByHr emissao1 = emissByHr.get(0);
        EmissByHr emissao2 = emissByHr.size() > 1 ? emissByHr.get(1) : new EmissByHr("valor vazio");



        String[] emitidos1 = emissao1.value().split(" ");
        String[] emitidos2 = emissao2.value().split(" ");

        String totalEmitidos1 = emitidos1[6];
        String totalEmitidos2 = emitidos2[2];

        int totalEmissao1 = Integer.parseInt(totalEmitidos1);
        int totalEmissao2 = Integer.parseInt(totalEmitidos2);

//        int difEmissao = Math.abs(totalEmissao1 - totalEmissao2);


        model.addAttribute("emissao1", totalEmissao1);
        model.addAttribute("emissao2", totalEmissao2);
        model.addAttribute("difEmissao", totalEmissao2);
        /* **********FIM BOX 4 (SEM EMISSÃO) ***********/

        /* **********GRAFICO 3(TEMPO MEDIO REGIONAL) ***********/
        String valorCompleto = dadosRegional.get(0).value();

        String[] linhasRegionais = valorCompleto.split("\n");

        List<Map<String, Object>> grafico3 = new ArrayList<>();

        for (String linha : linhasRegionais) {

            linha = linha.trim();

            String[] partes = linha.split("\\|");

            if (partes.length == 3) {
                String codEstado = partes[0].split("-")[0].trim();
                String nomeEstado = partes[0].split("-")[1].trim();
                String totalEstado = partes[1].split(":")[1].trim();
                String tempoMedio = partes[2].split(":")[1].trim();


                Map<String, Object> graficoRegional = new HashMap<>();
                graficoRegional.put("codUf", codEstado);
                graficoRegional.put("estado", nomeEstado);
                graficoRegional.put("totalEstado", totalEstado);
                graficoRegional.put("tempoRegional", tempoMedio);

                grafico3.add(graficoRegional);
            }
        }

        /* **********FIM GRAFICO 3(TEMPO MEDIO REGIONAL) ***********/

        model.addAttribute("linhas",linhas);
        model.addAttribute("grafico3", grafico3);

        return "nfce";
    }

}

