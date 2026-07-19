package cl.duoc.medialert.alertas.service;

import cl.duoc.medialert.alertas.model.Alerta;
import cl.duoc.medialert.alertas.repository.AlertaRepository;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlertaService {
    private final AlertaRepository repo;
    public AlertaService(AlertaRepository repo) { this.repo = repo; }

    public List<Alerta> listar(String estado, String pacienteRut) {
        if (estado != null && !estado.isBlank()) return repo.findByEstadoIgnoreCaseOrderByFechaHoraDesc(estado);
        if (pacienteRut != null && !pacienteRut.isBlank()) return repo.findByPacienteRutOrderByFechaHoraDesc(pacienteRut);
        return repo.findAll().stream().sorted((a,b) -> b.getFechaHora().compareTo(a.getFechaHora())).toList();
    }
    public Alerta obtener(Long id) { return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Alerta no encontrada")); }
    @Transactional public Alerta crear(Alerta a) {
        a.setId(null); if (a.getFechaHora() == null) a.setFechaHora(Instant.now()); if (a.getEstado() == null) a.setEstado("NUEVA");
        return repo.save(a);
    }
    @Transactional public Alerta actualizar(Long id, Alerta n) {
        Alerta a = obtener(id);
        if (n.getEstado() != null) a.setEstado(n.getEstado());
        if (n.getDetalle() != null) a.setDetalle(n.getDetalle());
        if (n.getSeveridad() != null) a.setSeveridad(n.getSeveridad());
        return repo.save(a);
    }
    @Transactional public void eliminar(Long id) { repo.delete(obtener(id)); }
}
