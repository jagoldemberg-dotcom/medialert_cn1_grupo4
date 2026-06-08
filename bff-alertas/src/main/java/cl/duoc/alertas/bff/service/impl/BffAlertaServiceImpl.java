package cl.duoc.alertas.bff.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import cl.duoc.alertas.bff.client.AlertaVitalClient;
import cl.duoc.alertas.bff.dto.AlertaVitalRequest;
import cl.duoc.alertas.bff.dto.AlertaVitalResponse;
import cl.duoc.alertas.bff.dto.EstadoAlertaUpdateRequest;
import cl.duoc.alertas.bff.service.BffAlertaService;

@Service
public class BffAlertaServiceImpl implements BffAlertaService {
    private final AlertaVitalClient alertaVitalClient;

    public BffAlertaServiceImpl(AlertaVitalClient alertaVitalClient) {
        this.alertaVitalClient = alertaVitalClient;
    }

    @Override
    public List<AlertaVitalResponse> listar(String estado, String pacienteRut) {
        return alertaVitalClient.listar(estado, pacienteRut);
    }

    @Override
    public AlertaVitalResponse buscarPorId(Long id) {
        return alertaVitalClient.buscarPorId(id);
    }

    @Override
    public AlertaVitalResponse crear(AlertaVitalRequest request) {
        return alertaVitalClient.crear(request);
    }

    @Override
    public AlertaVitalResponse actualizar(Long id, AlertaVitalRequest request) {
        return alertaVitalClient.actualizar(id, request);
    }

    @Override
    public AlertaVitalResponse cambiarEstado(Long id, EstadoAlertaUpdateRequest request) {
        return alertaVitalClient.cambiarEstado(id, request);
    }

    @Override
    public void eliminar(Long id) {
        alertaVitalClient.eliminar(id);
    }
}
