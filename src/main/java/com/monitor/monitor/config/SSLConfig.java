package com.monitor.monitor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

@Configuration
public class SSLConfig {

    @Bean
    public RestTemplate restTemplate() throws Exception {
        // Cria o TrustManager para aceitar todos os certificados
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        } };

        // Configura o contexto SSL para ignorar a verificação do certificado
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new SecureRandom());

        // Define o SSLContext para ser usado por todas as conexões HTTPS
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Retorna o RestTemplate, agora configurado para ignorar SSL
        return new RestTemplate();
    }
}
