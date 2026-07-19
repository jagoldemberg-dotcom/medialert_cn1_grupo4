package cl.duoc.medialert.streaming.service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class StreamingService {
    private final Deque<Map<String,Object>> events = new ConcurrentLinkedDeque<>();
    private final int maxEvents;
    public StreamingService(@Value("${medialert.streaming.max-events:200}") int maxEvents) { this.maxEvents = maxEvents; }

    @KafkaListener(topics = {"alertas-medicas", "resumenes-vitales"}, groupId = "streaming-alertas")
    public void consume(Map<String,Object> event) {
        Map<String,Object> copy = new LinkedHashMap<>(event);
        copy.putIfAbsent("recibidoEn", Instant.now().toString());
        events.addFirst(copy);
        while (events.size() > maxEvents) events.pollLast();
    }

    public List<Map<String,Object>> recent(int limit) {
        return events.stream().limit(Math.max(1, Math.min(limit, 200))).toList();
    }

    public Map<String,Object> stats() {
        Map<String,Long> byType = new TreeMap<>();
        Map<String,Long> bySeverity = new TreeMap<>();
        events.forEach(e -> {
            byType.merge(String.valueOf(e.getOrDefault("tipoEvento", "DESCONOCIDO")), 1L, Long::sum);
            bySeverity.merge(String.valueOf(e.getOrDefault("severidad", "SIN_CLASIFICAR")), 1L, Long::sum);
        });
        return Map.of("total", events.size(), "porTipo", byType, "porSeveridad", bySeverity, "actualizadoEn", Instant.now());
    }
}
