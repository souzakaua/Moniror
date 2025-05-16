package com.monitor.monitor.controller;

import com.monitor.monitor.api.ApiZabbix;
import com.monitor.monitor.records.*;
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
@RequestMapping("/nfe")
public class NfeController {


    private final ApiZabbix apiZabbix;

    public NfeController(ApiZabbix apiZabbix) {
        this.apiZabbix = apiZabbix;
    }

    @GetMapping
    public String nfce(Model model) {
        List<TempoMedio> temponfce = apiZabbix.buscarTempoMedioNfe();
        List<EmissByHr> emissByHr = apiZabbix.buscarEmissByHrNfe();
        List<TotalNfDia> totalDiario = apiZabbix.buscarTotalNfDiaNfe();
        List<BuscaRegional> dadosRegional = apiZabbix.BuscaRegionalNfe();
        List<Map<String, Object>> linhas = new ArrayList<>();
        List<BuscaSemEmissao> semEmicao = apiZabbix.BuscaSemEmissao();

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

        /* **********BOX 4 (EMISSÃO POR HORA) ***********/
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
        model.addAttribute("difEmissao", totalEmissao2);
        /* **********FIM BOX 4 (EMISSÃO POR HORA) ***********/

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

        /* **********INICIO TABELA 2(FILIAIS QUE NÃO EMITIRAM) ***********/

        String filiaisSemEmissao = semEmicao.get(0).value();

// Lista final com os dados
        List<Map<String, Object>> tabela2 = new ArrayList<>();

// Quebra os blocos de cada filial
        String[] blocos = filiaisSemEmissao.split("<E>");

        for (String bloco : blocos) {
            bloco = bloco.replace("</E>", "").trim();

            if (bloco.isEmpty() || !bloco.contains("|")) continue;

            try {
                String[] partes = bloco.split("\\|");
                if (partes.length != 2) continue;

                String parteFilial = partes[0].trim();
                String parteEmissao = partes[1].trim();

                String[] dadosFilial = parteFilial.split("-");

                String numF = dadosFilial[0].trim();
                String nomeF = parteFilial.substring(parteFilial.indexOf("-") + 1, parteFilial.indexOf("- UF")).trim();
                String uf = parteFilial.substring(parteFilial.lastIndexOf(":") + 1).trim();

                // Corrigido: captura a data e hora corretamente
                String dataHora = parteEmissao.replace("Ultima emissao:", "").trim();
                String[] dataHoraSplit = dataHora.split(" ");
                String data = dataHoraSplit[0];
                String hora = dataHoraSplit[1];

                Map<String, Object> mapa = new HashMap<>();
                mapa.put("numF", numF);
                mapa.put("nomeF", nomeF);
                mapa.put("uf", uf);
                mapa.put("data", data);
                mapa.put("hora", hora);

                tabela2.add(mapa);
            } catch (Exception e) {
                System.out.println("Erro ao processar bloco: " + bloco);
                e.printStackTrace();
            }
        }


        model.addAttribute("tabelaSemEmissao", tabela2);
        /* **********FIM TABELA 2(FILIAIS QUE NÃO EMITIRAM) ***********/

        model.addAttribute("linhas",linhas);
        model.addAttribute("grafico3", grafico3);

        return "nfe";
    }

}
