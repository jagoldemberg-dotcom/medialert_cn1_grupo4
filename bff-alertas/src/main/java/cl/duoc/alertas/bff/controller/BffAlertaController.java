package cl.duoc.alertas.bff.controller;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;

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

import cl.duoc.alertas.bff.dto.AlertaVitalRequest;
import cl.duoc.alertas.bff.dto.AlertaVitalResponse;
import cl.duoc.alertas.bff.dto.EstadoAlertaUpdateRequest;
import cl.duoc.alertas.bff.service.BffAlertaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/bff/alertas")
@Validated
public class BffAlertaController {
    private final BffAlertaService bffAlertaService;

    public BffAlertaController(BffAlertaService bffAlertaService) {
        this.bffAlertaService = bffAlertaService;
    }

    @GetMapping
    public ResponseEntity<List<AlertaVitalResponse>> listar(
            @RequestParam(name = "estado", required = false) String estado,
            @RequestParam(name = "pacienteRut", required = false) String pacienteRut) {
        return ResponseEntity.ok(bffAlertaService.listar(estado, pacienteRut));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertaVitalResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(bffAlertaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<AlertaVitalResponse> crear(@Valid @RequestBody AlertaVitalRequest request) {
        AlertaVitalResponse creada = bffAlertaService.crear(request);
        return ResponseEntity.created(URI.create("/api/bff/alertas/" + creada.getId())).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlertaVitalResponse> actualizar(@PathVariable Long id,
                                                          @Valid @RequestBody AlertaVitalRequest request) {
        return ResponseEntity.ok(bffAlertaService.actualizar(id, request));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<AlertaVitalResponse> cambiarEstado(@PathVariable Long id,
                                                             @Valid @RequestBody EstadoAlertaUpdateRequest request) {
        return ResponseEntity.ok(bffAlertaService.cambiarEstado(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        bffAlertaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario")
    public ResponseEntity<Map<String, String>> usuarioActual(Principal principal) {
        return ResponseEntity.ok(Map.of("usuario", principal == null ? "anonimo" : principal.getName()));
    }
}
