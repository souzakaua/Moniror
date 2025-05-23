package com.monitor.monitor.controller;


import com.monitor.monitor.api.ApiZabbix;
import com.monitor.monitor.records.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
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
        List<TesteApi> emissaoHoraTeste = apiZabbix.buscarTabelaTempoMedio();
        List<TotalNfDia> totalDiario = apiZabbix.buscarTotalNfDia();
        List<BuscaRegional> dadosRegional = apiZabbix.BuscaRegional();
        List<Map<String, Object>> linhas = new ArrayList<>();

        /*TABELA 1 TEMPO MEDIO*/

        String temp = emissaoHoraTeste.get(0).value();

        String[] linhasTemp = temp.split("\n");

        List<Map<String, Object>> table1 = new ArrayList<>();

        DecimalFormat formate = new DecimalFormat("#,##0");

        for (String linha : linhasTemp) {

            linha = linha.trim();

            String[] partes = linha.split("\\|");

            if (partes.length == 3) {
                String dataTabela1 = partes[0].split(" ")[0].trim();
                String horaTabela1 = partes[0].split(" ")[1].trim();
                String totalTabela1 = partes[1].split(":")[1].trim();
                String mediaTabela1 = partes[2].split(":")[1].trim();

                DecimalFormat formater = new DecimalFormat("#,##0");
                double totalTabelaF = Double.parseDouble(totalTabela1);
                String totalTabelaForm = formater.format(totalTabelaF);


                LocalDate dataConvertida = LocalDate.parse(dataTabela1);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String dataFormatada = dataConvertida.format(formatter);

                LocalTime horaConvertida = LocalTime.parse(horaTabela1);
                String horaFormatada = horaConvertida.format(DateTimeFormatter.ofPattern("HH:mm"));

                Map<String, Object> tabelaTempo = new HashMap<>();
                tabelaTempo.put("dataTabela1", dataFormatada);
                tabelaTempo.put("totalTabelaF", totalTabelaForm);
                tabelaTempo.put("horaTabela1", horaFormatada);
                tabelaTempo.put("totalTabela1", totalTabela1);
                tabelaTempo.put("mediaTabela1", mediaTabela1);

                table1.add(tabelaTempo);

            if (table1.size() >= 2) {
                try {
                    double total1 = Double.parseDouble((String) table1.get(0).get("totalTabela1"));
                    double total2 = Double.parseDouble((String) table1.get(1).get("totalTabela1"));
                    double diferenca = Math.abs(total1 - total2);


                    String documentos1Formatado = formate.format(total1);

                    int totalMedia1 = Integer.parseInt((String) table1.get(0).get("mediaTabela1"));
                    int totalMedia2 = Integer.parseInt((String) table1.get(1).get("mediaTabela1"));
                    int diferencaMedia = Math.abs(totalMedia1 - totalMedia2 );

                    model.addAttribute("documentosForm", documentos1Formatado);
                    model.addAttribute("documentos1", total1);
                    model.addAttribute("documentos2", total2);
                    model.addAttribute("difDoc", diferenca);

                    model.addAttribute("media1", totalMedia1);
                    model.addAttribute("media2", totalMedia2);
                    model.addAttribute("difMedia", diferencaMedia);
                    model.addAttribute("difMedia", diferencaMedia);
                } catch (NumberFormatException e) {
                    model.addAttribute("diferencaTotal", 0); // fallback em caso de erro
                }
            }
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

//        TempoMedio totalDoc1 = temponfce.get(0);
//        TempoMedio totalDoc2 = temponfce.size() > 1 ? temponfce.get(1) : new TempoMedio("valor vazio");
//
//        String[] doc1 = totalDoc1.value().split(" ");
//        String[] doc2 = totalDoc2.value().split(" ");
//
//        String documentos1 = doc1[4];
//        String documentos2 = doc2[4];
//
//        int notasAnteInt = Integer.parseInt(documentos1);
//        int notasAtualInt = Integer.parseInt(documentos2);
//
//        int difDoc = Math.abs(notasAtualInt - notasAnteInt);
//
//
//        model.addAttribute("documentos2",documentos2);
//        model.addAttribute("difDoc",difDoc);
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
        EmissByHr emissao2 = emissByHr.get(0);



        String[] emitidos1 = emissao1.value().split(" ");
        String[] emitidos2 = emissao2.value().split(" ");

        String totalEmitidos1 = emitidos1[6];
        String totalEmitidos2 = emitidos2[2];

        double totalEmissao1 = Double.parseDouble(totalEmitidos1);
        int totalEmissao2 = Integer.parseInt(totalEmitidos2);

        String totalEmissaoForm = formate.format(totalEmissao1);

//        int difEmissao = Math.abs(totalEmissao1 - totalEmissao2);


        model.addAttribute("emissao1", totalEmissaoForm);
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

