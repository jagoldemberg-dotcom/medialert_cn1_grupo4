package cl.duoc.alertas.bff.service;

import java.util.List;

import cl.duoc.alertas.bff.dto.AlertaVitalRequest;
import cl.duoc.alertas.bff.dto.AlertaVitalResponse;
import cl.duoc.alertas.bff.dto.EstadoAlertaUpdateRequest;

public interface BffAlertaService {
    List<AlertaVitalResponse> listar(String estado, String pacienteRut);
    AlertaVitalResponse buscarPorId(Long id);
    AlertaVitalResponse crear(AlertaVitalRequest request);
    AlertaVitalResponse actualizar(Long id, AlertaVitalRequest request);
    AlertaVitalResponse cambiarEstado(Long id, EstadoAlertaUpdateRequest request);
    void eliminar(Long id);
}
