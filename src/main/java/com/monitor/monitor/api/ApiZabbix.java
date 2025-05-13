package com.monitor.monitor.api;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitor.monitor.api.records.TempoMedio;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApiZabbix {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    public List<TempoMedio> buscarTempoMedio() {
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

        TempoNfce.forEach(item -> System.out.println(item.value()));
        return TempoNfce;
    }
}
