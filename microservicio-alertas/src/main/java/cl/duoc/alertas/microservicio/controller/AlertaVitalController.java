package cl.duoc.alertas.microservicio.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.alertas.microservicio.dto.AlertaVitalRequest;
import cl.duoc.alertas.microservicio.dto.AlertaVitalResponse;
import cl.duoc.alertas.microservicio.dto.EstadoAlertaUpdateRequest;
import cl.duoc.alertas.microservicio.service.AlertaVitalService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/microservicio/alertas")
@Validated
public class AlertaVitalController {
    private final AlertaVitalService alertaVitalService;

    public AlertaVitalController(AlertaVitalService alertaVitalService) {
        this.alertaVitalService = alertaVitalService;
    }

    @GetMapping
    public ResponseEntity<List<AlertaVitalResponse>> listar(
            @RequestParam(name = "estado", required = false) String estado,
            @RequestParam(name = "pacienteRut", required = false) String pacienteRut) {
        return ResponseEntity.ok(alertaVitalService.listar(estado, pacienteRut));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertaVitalResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(alertaVitalService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<AlertaVitalResponse> crear(@Valid @RequestBody AlertaVitalRequest request) {
        AlertaVitalResponse creada = alertaVitalService.crear(request);
        return ResponseEntity.created(URI.create("/api/microservicio/alertas/" + creada.getId())).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlertaVitalResponse> actualizar(@PathVariable Long id,
                                                          @Valid @RequestBody AlertaVitalRequest request) {
        return ResponseEntity.ok(alertaVitalService.actualizar(id, request));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<AlertaVitalResponse> cambiarEstado(@PathVariable Long id,
                                                             @Valid @RequestBody EstadoAlertaUpdateRequest request) {
        return ResponseEntity.ok(alertaVitalService.cambiarEstado(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        alertaVitalService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
