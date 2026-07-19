package cl.duoc.medialert.alertas.controller;

import cl.duoc.medialert.alertas.model.Alerta;
import cl.duoc.medialert.alertas.service.AlertaService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alertas")
public class AlertaController {
    private final AlertaService service;
    public AlertaController(AlertaService service) { this.service = service; }
    @GetMapping public List<Alerta> listar(@RequestParam(required=false) String estado, @RequestParam(required=false) String pacienteRut) { return service.listar(estado, pacienteRut); }
    @GetMapping("/{id}") public Alerta obtener(@PathVariable Long id) { return service.obtener(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public Alerta crear(@RequestBody Alerta a) { return service.crear(a); }
    @PutMapping("/{id}") public Alerta actualizar(@PathVariable Long id, @RequestBody Alerta a) { return service.actualizar(id, a); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
