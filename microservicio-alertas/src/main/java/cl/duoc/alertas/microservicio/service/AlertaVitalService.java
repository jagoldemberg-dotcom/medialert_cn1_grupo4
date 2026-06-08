package cl.duoc.alertas.microservicio.service;

import java.util.List;

import cl.duoc.alertas.microservicio.dto.AlertaVitalRequest;
import cl.duoc.alertas.microservicio.dto.AlertaVitalResponse;
import cl.duoc.alertas.microservicio.dto.EstadoAlertaUpdateRequest;

public interface AlertaVitalService {
    List<AlertaVitalResponse> listar(String estado, String pacienteRut);
    AlertaVitalResponse buscarPorId(Long id);
    AlertaVitalResponse crear(AlertaVitalRequest request);
    AlertaVitalResponse actualizar(Long id, AlertaVitalRequest request);
    AlertaVitalResponse cambiarEstado(Long id, EstadoAlertaUpdateRequest request);
    void eliminar(Long id);
}
