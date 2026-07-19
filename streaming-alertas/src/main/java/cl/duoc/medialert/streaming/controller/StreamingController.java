package cl.duoc.medialert.streaming.controller;

import cl.duoc.medialert.streaming.service.StreamingService;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/streaming")
public class StreamingController {
    private final StreamingService service;
    public StreamingController(StreamingService service) { this.service = service; }
    @GetMapping("/health") public Map<String,Object> health() { return Map.of("component","streaming-alertas","status","UP","timestamp",Instant.now()); }
    @GetMapping("/eventos") public List<Map<String,Object>> eventos(@RequestParam(defaultValue="20") int limite) { return service.recent(limite); }
    @GetMapping("/estadisticas") public Map<String,Object> estadisticas() { return service.stats(); }
}
