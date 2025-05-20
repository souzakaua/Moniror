package com.monitor.monitor.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitor.monitor.records.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApiZabbix {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public ApiZabbix(RestTemplate restTemplate) {
        this.restTemplate = restTemplate; // Injeção do RestTemplate com SSL configurado
    }

    //NFCE
    public List<TempoMedio> buscarTempoMedio() {
        String url = "https://srvzabbixweb.br-atacadao.corp/api_jsonrpc.php";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("jsonrpc", "2.0");
        requestBody.put("method", "history.get");

        Map<String, Object> params = new HashMap<>();
        params.put("output", "extend");
        params.put("history", 4);
        params.put("itemids", "3724011");
        params.put("sortfield", "clock");
        params.put("sortorder", "DESC");
        params.put("limit", 30);

        requestBody.put("params", params);
        requestBody.put("id", 5);
        requestBody.put("auth", "78e6ebb232ef422e3c2443256c4b6ac66eeebd2c9975cf90b9e02626d4f3ad22");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        Map<String, Object> resposta = response.getBody();

        List<TempoMedio> TempoNfce = mapper.convertValue(
                resposta.get("result"), new TypeReference<List<TempoMedio>>() {
                }
        );
        return TempoNfce;
    }

    public List<EmissByHr> buscarEmissByHr() {
        String url = "https://srvzabbixweb.br-atacadao.corp/api_jsonrpc.php";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("jsonrpc", "2.0");
        requestBody.put("method", "history.get");

        Map<String, Object> params = new HashMap<>();
        params.put("output", "extend");
        params.put("history", 4);
        params.put("itemids", "3739000");
        params.put("sortfield", "clock");
        params.put("sortorder", "DESC");
        params.put("limit", 12);

        requestBody.put("params", params);
        requestBody.put("id", 5);
        requestBody.put("auth", "78e6ebb232ef422e3c2443256c4b6ac66eeebd2c9975cf90b9e02626d4f3ad22");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        Map<String, Object> resposta = response.getBody();

        List<EmissByHr> emissByHr = mapper.convertValue(
                resposta.get("result"), new TypeReference<List<EmissByHr>>() {
                }
        );

        return emissByHr;
    }

    public List<TotalNfDia> buscarTotalNfDia() {
        String url = "https://srvzabbixweb.br-atacadao.corp/api_jsonrpc.php";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("jsonrpc", "2.0");
        requestBody.put("method", "history.get");

        Map<String, Object> params = new HashMap<>();
        params.put("output", "extend");
        params.put("history", 4);
        params.put("itemids", "3738580");
        params.put("sortfield", "clock");
        params.put("sortorder", "DESC");
        params.put("limit", 3);

        requestBody.put("params", params);
        requestBody.put("id", 5);
        requestBody.put("auth", "78e6ebb232ef422e3c2443256c4b6ac66eeebd2c9975cf90b9e02626d4f3ad22");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        Map<String, Object> resposta = response.getBody();

        List<TotalNfDia> totalNfDia = mapper.convertValue(
                resposta.get("result"), new TypeReference<List<TotalNfDia>>() {
                }
        );

        return totalNfDia;
    }

    public List<BuscaRegional> BuscaRegional() {
        String url = "https://srvzabbixweb.br-atacadao.corp/api_jsonrpc.php";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("jsonrpc", "2.0");
        requestBody.put("method", "history.get");

        Map<String, Object> params = new HashMap<>();
        params.put("output", "extend");
        params.put("history", 4);
        params.put("itemids", "3724012");
        params.put("sortfield", "clock");
        params.put("sortorder", "DESC");
        params.put("limit", 1);

        requestBody.put("params", params);
        requestBody.put("id", 5);
        requestBody.put("auth", "78e6ebb232ef422e3c2443256c4b6ac66eeebd2c9975cf90b9e02626d4f3ad22");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        Map<String, Object> resposta = response.getBody();

        List<BuscaRegional> dadosRegionais = mapper.convertValue(
                resposta.get("result"), new TypeReference<List<BuscaRegional>>() {
                }
        );
        return dadosRegionais;
    }

    public List<TesteApi> buscarTabelaTempoMedio() {
        String url = "https://srvzabbixweb.br-atacadao.corp/api_jsonrpc.php";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("jsonrpc", "2.0");
        requestBody.put("method", "history.get");

        Map<String, Object> params = new HashMap<>();
        params.put("output", "extend");
        params.put("history", 4);
        params.put("itemids", "3740025");
        params.put("sortfield", "clock");
        params.put("sortorder", "DESC");
        params.put("limit", 1);

        requestBody.put("params", params);
        requestBody.put("id", 5);
        requestBody.put("auth", "78e6ebb232ef422e3c2443256c4b6ac66eeebd2c9975cf90b9e02626d4f3ad22");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        Map<String, Object> resposta = response.getBody();

        List<TesteApi> testeEmissHora = mapper.convertValue(
                resposta.get("result"), new TypeReference<>() {
                }
        );
        return testeEmissHora;
    }




    //NFE
    public List<TempoMedio> buscarTempoMedioNfe() {
        String url = "https://srvzabbixweb.br-atacadao.corp/api_jsonrpc.php";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("jsonrpc", "2.0");
        requestBody.put("method", "history.get");

        Map<String, Object> params = new HashMap<>();
        params.put("output", "extend");
        params.put("history", 4);
        params.put("itemids", "3723803");
        params.put("sortfield", "clock");
        params.put("sortorder", "DESC");
        params.put("limit", 30);

        requestBody.put("params", params);
        requestBody.put("id", 5);
        requestBody.put("auth", "78e6ebb232ef422e3c2443256c4b6ac66eeebd2c9975cf90b9e02626d4f3ad22");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        Map<String, Object> resposta = response.getBody();

        List<TempoMedio> TempoNfce = mapper.convertValue(
                resposta.get("result"), new TypeReference<List<TempoMedio>>() {
                }
        );

        return TempoNfce;
    }

    public List<EmissByHr> buscarEmissByHrNfe() {
        String url = "https://srvzabbixweb.br-atacadao.corp/api_jsonrpc.php";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("jsonrpc", "2.0");
        requestBody.put("method", "history.get");

        Map<String, Object> params = new HashMap<>();
        params.put("output", "extend");
        params.put("history", 4);
        params.put("itemids", "3731699");
        params.put("sortfield", "clock");
        params.put("sortorder", "DESC");
        params.put("limit", 12);

        requestBody.put("params", params);
        requestBody.put("id", 5);
        requestBody.put("auth", "78e6ebb232ef422e3c2443256c4b6ac66eeebd2c9975cf90b9e02626d4f3ad22");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        Map<String, Object> resposta = response.getBody();

        List<EmissByHr> emissByHr = mapper.convertValue(
                resposta.get("result"), new TypeReference<List<EmissByHr>>() {
                }
        );

        return emissByHr;
    }

    public List<TotalNfDia> buscarTotalNfDiaNfe() {
        String url = "https://srvzabbixweb.br-atacadao.corp/api_jsonrpc.php";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("jsonrpc", "2.0");
        requestBody.put("method", "history.get");

        Map<String, Object> params = new HashMap<>();
        params.put("output", "extend");
        params.put("history", 4);
        params.put("itemids", "3712327");
        params.put("sortfield", "clock");
        params.put("sortorder", "DESC");
        params.put("limit", 3);

        requestBody.put("params", params);
        requestBody.put("id", 5);
        requestBody.put("auth", "78e6ebb232ef422e3c2443256c4b6ac66eeebd2c9975cf90b9e02626d4f3ad22");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        Map<String, Object> resposta = response.getBody();

        List<TotalNfDia> totalNfDia = mapper.convertValue(
                resposta.get("result"), new TypeReference<List<TotalNfDia>>() {
                }
        );

        return totalNfDia;
    }

    public List<BuscaRegional> BuscaRegionalNfe() {
        String url = "https://srvzabbixweb.br-atacadao.corp/api_jsonrpc.php";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("jsonrpc", "2.0");
        requestBody.put("method", "history.get");

        Map<String, Object> params = new HashMap<>();
        params.put("output", "extend");
        params.put("history", 4);
        params.put("itemids", "3738988");
        params.put("sortfield", "clock");
        params.put("sortorder", "DESC");
        params.put("limit", 1);

        requestBody.put("params", params);
        requestBody.put("id", 5);
        requestBody.put("auth", "78e6ebb232ef422e3c2443256c4b6ac66eeebd2c9975cf90b9e02626d4f3ad22");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        Map<String, Object> resposta = response.getBody();

        List<BuscaRegional> dadosRegionais = mapper.convertValue(
                resposta.get("result"), new TypeReference<>() {
                }
        );
        return dadosRegionais;
    }

    public List<BuscaSemEmissao> BuscaSemEmissao() {
        String url = "https://srvzabbixweb.br-atacadao.corp/api_jsonrpc.php";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("jsonrpc", "2.0");
        requestBody.put("method", "history.get");

        Map<String, Object> params = new HashMap<>();
        params.put("output", "extend");
        params.put("history", 4);
        params.put("itemids", "3723805");
        params.put("sortfield", "clock");
        params.put("sortorder", "DESC");
        params.put("limit", 1);

        requestBody.put("params", params);
        requestBody.put("id", 5);
        requestBody.put("auth", "78e6ebb232ef422e3c2443256c4b6ac66eeebd2c9975cf90b9e02626d4f3ad22");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        Map<String, Object> resposta = response.getBody();

        List<BuscaSemEmissao> semEmissao = mapper.convertValue(
                resposta.get("result"), new TypeReference<>() {
                }
        );
        return semEmissao;
    }

    public List<TesteApi> buscarTabelaTempoMedioNfe() {
        String url = "https://srvzabbixweb.br-atacadao.corp/api_jsonrpc.php";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("jsonrpc", "2.0");
        requestBody.put("method", "history.get");

        Map<String, Object> params = new HashMap<>();
        params.put("output", "extend");
        params.put("history", 4);
        params.put("itemids", "3740025");
        params.put("sortfield", "clock");
        params.put("sortorder", "DESC");
        params.put("limit", 1);

        requestBody.put("params", params);
        requestBody.put("id", 5);
        requestBody.put("auth", "78e6ebb232ef422e3c2443256c4b6ac66eeebd2c9975cf90b9e02626d4f3ad22");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        Map<String, Object> resposta = response.getBody();

        List<TesteApi> testeEmissHora = mapper.convertValue(
                resposta.get("result"), new TypeReference<>() {
                }
        );
        return testeEmissHora;
    }

}

