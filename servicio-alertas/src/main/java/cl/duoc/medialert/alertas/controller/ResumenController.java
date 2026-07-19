package cl.duoc.medialert.alertas.controller;

import cl.duoc.medialert.alertas.model.ResumenVital;
import cl.duoc.medialert.alertas.repository.ResumenVitalRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resumenes")
public class ResumenController {
    private final ResumenVitalRepository repo;
    public ResumenController(ResumenVitalRepository repo) { this.repo = repo; }
    @GetMapping public List<ResumenVital> listar() { return repo.findAll().stream().sorted((a,b)->b.getFechaHora().compareTo(a.getFechaHora())).toList(); }
    @GetMapping("/{id}") public ResumenVital obtener(@PathVariable Long id) { return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Resumen no encontrado")); }
}
