package cl.duoc.alertas.microservicio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.alertas.microservicio.entity.AlertaVital;

public interface AlertaVitalRepository extends JpaRepository<AlertaVital, Long> {
    List<AlertaVital> findByEstadoIgnoreCaseOrderByFechaRegistroDesc(String estado);
    List<AlertaVital> findByPacienteRutContainingIgnoreCaseOrderByFechaRegistroDesc(String pacienteRut);
    List<AlertaVital> findByEstadoIgnoreCaseAndPacienteRutContainingIgnoreCaseOrderByFechaRegistroDesc(String estado, String pacienteRut);
    List<AlertaVital> findAllByOrderByFechaRegistroDesc();
}
