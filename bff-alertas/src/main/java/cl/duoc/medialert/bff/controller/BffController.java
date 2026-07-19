package cl.duoc.medialert.bff.controller;

import cl.duoc.medialert.bff.dto.ColaResponse;
import cl.duoc.medialert.bff.dto.ResumenVitalRequest;
import cl.duoc.medialert.bff.dto.SenalVitalRequest;
import cl.duoc.medialert.bff.service.ColaService;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bff")
public class BffController {
    private final ColaService colaService;

    public BffController(ColaService colaService) {
        this.colaService = colaService;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of("component", "bff-alertas", "status", "UP", "timestamp", Instant.now());
    }

    @PostMapping("/colas/senales")
    public ResponseEntity<ColaResponse> senal(@Valid @RequestBody SenalVitalRequest request) {
        if (request.umbralMinimo() >= request.umbralMaximo()) {
            throw new IllegalArgumentException("El umbral mínimo debe ser menor que el máximo");
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(colaService.publicarSenal(request));
    }

    @PostMapping("/colas/resumenes")
    public ResponseEntity<ColaResponse> resumen(@Valid @RequestBody ResumenVitalRequest request) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(colaService.publicarResumen(request));
    }
}
