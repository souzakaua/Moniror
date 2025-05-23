import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class apiCon {

    public static void main(String[] args) throws IOException, InterruptedException, NoSuchAlgorithmException {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) { }
                }
        };

        SSLContext sslContext = SSLContext.getInstance("TLS");
        try {
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        HttpClient client = HttpClient.newBuilder()
                .sslContext(sslContext)
                .build();

        String json = """
        {
            "jsonrpc": "2.0",
            "method": "history.get",
            "params": {
                "output": "extend",
                "history": 4,
                "itemids": "3724011",
                "sortfield": "clock",
                "sortorder": "DESC",
                "limit": 10
            },
            "id": 5,
            "auth": "78e6ebb232ef422e3c2443256c4b6ac66eeebd2c9975cf90b9e02626d4f3ad22"
        }
        """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://srvzabbixweb.br-atacadao.corp/api_jsonrpc.php"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
    }
}
