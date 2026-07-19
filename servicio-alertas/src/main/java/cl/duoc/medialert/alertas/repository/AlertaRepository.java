package cl.duoc.medialert.alertas.repository;
import cl.duoc.medialert.alertas.model.Alerta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AlertaRepository extends JpaRepository<Alerta, Long> {
    List<Alerta> findByEstadoIgnoreCaseOrderByFechaHoraDesc(String estado);
    List<Alerta> findByPacienteRutOrderByFechaHoraDesc(String pacienteRut);
}
