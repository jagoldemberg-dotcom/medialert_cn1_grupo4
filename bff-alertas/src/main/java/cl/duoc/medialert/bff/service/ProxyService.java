package cl.duoc.medialert.bff.service;

import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ProxyService {
    private final RestClient client = RestClient.create();
    private final String alertasUrl;
    private final String streamingUrl;

    public ProxyService(@Value("${medialert.services.alertas-url}") String alertasUrl,
                        @Value("${medialert.services.streaming-url}") String streamingUrl) {
        this.alertasUrl = alertasUrl;
        this.streamingUrl = streamingUrl;
    }

    public ResponseEntity<String> alertas(HttpMethod method, String path, String query, String body) {
        return exchange(method, alertasUrl + "/api/alertas" + path, query, body);
    }

    public ResponseEntity<String> resumenes(HttpMethod method, String path, String query, String body) {
        return exchange(method, alertasUrl + "/api/resumenes" + path, query, body);
    }

    public ResponseEntity<String> streaming(HttpMethod method, String path, String query) {
        return exchange(method, streamingUrl + "/api/streaming" + path, query, null);
    }

    private ResponseEntity<String> exchange(HttpMethod method, String base, String query, String body) {
        URI uri = UriComponentsBuilder.fromUriString(base)
                .query(query == null ? "" : query)
                .build(true).toUri();
        var spec = client.method(method).uri(uri).header("Content-Type", "application/json");
        if (body != null && !body.isBlank()) {
            spec.body(body);
        }
        return spec.retrieve().toEntity(String.class);
    }
}
