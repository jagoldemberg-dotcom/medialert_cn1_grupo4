package cl.duoc.alertas.microservicio.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.duoc.alertas.microservicio.dto.AlertaVitalRequest;
import cl.duoc.alertas.microservicio.dto.AlertaVitalResponse;
import cl.duoc.alertas.microservicio.dto.EstadoAlertaUpdateRequest;
import cl.duoc.alertas.microservicio.entity.AlertaVital;
import cl.duoc.alertas.microservicio.exception.RecursoNoEncontradoException;
import cl.duoc.alertas.microservicio.repository.AlertaVitalRepository;
import cl.duoc.alertas.microservicio.service.AlertaVitalService;

@Service
@Transactional
public class AlertaVitalServiceImpl implements AlertaVitalService {
    private final AlertaVitalRepository alertaVitalRepository;

    public AlertaVitalServiceImpl(AlertaVitalRepository alertaVitalRepository) {
        this.alertaVitalRepository = alertaVitalRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlertaVitalResponse> listar(String estado, String pacienteRut) {
        List<AlertaVital> alertas;
        boolean filtraEstado = estado != null && !estado.isBlank();
        boolean filtraPaciente = pacienteRut != null && !pacienteRut.isBlank();

        if (filtraEstado && filtraPaciente) {
            alertas = alertaVitalRepository.findByEstadoIgnoreCaseAndPacienteRutContainingIgnoreCaseOrderByFechaRegistroDesc(estado, pacienteRut);
        } else if (filtraEstado) {
            alertas = alertaVitalRepository.findByEstadoIgnoreCaseOrderByFechaRegistroDesc(estado);
        } else if (filtraPaciente) {
            alertas = alertaVitalRepository.findByPacienteRutContainingIgnoreCaseOrderByFechaRegistroDesc(pacienteRut);
        } else {
            alertas = alertaVitalRepository.findAllByOrderByFechaRegistroDesc();
        }
        return alertas.stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AlertaVitalResponse buscarPorId(Long id) {
        return toResponse(buscarEntidad(id));
    }

    @Override
    public AlertaVitalResponse crear(AlertaVitalRequest request) {
        validarUmbrales(request);
        AlertaVital alerta = new AlertaVital();
        cargarDatos(alerta, request);
        alerta.setEstado("NUEVA");
        aplicarEvaluacion(alerta);
        return toResponse(alertaVitalRepository.save(alerta));
    }

    @Override
    public AlertaVitalResponse actualizar(Long id, AlertaVitalRequest request) {
        validarUmbrales(request);
        AlertaVital alerta = buscarEntidad(id);
        cargarDatos(alerta, request);
        aplicarEvaluacion(alerta);
        return toResponse(alertaVitalRepository.save(alerta));
    }

    @Override
    public AlertaVitalResponse cambiarEstado(Long id, EstadoAlertaUpdateRequest request) {
        AlertaVital alerta = buscarEntidad(id);
        alerta.setEstado(request.getEstado().toUpperCase());
        if (request.getObservacion() != null && !request.getObservacion().isBlank()) {
            alerta.setObservacion(request.getObservacion());
        }
        return toResponse(alertaVitalRepository.save(alerta));
    }

    @Override
    public void eliminar(Long id) {
        AlertaVital alerta = buscarEntidad(id);
        alertaVitalRepository.delete(alerta);
    }

    private AlertaVital buscarEntidad(Long id) {
        return alertaVitalRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontraron datos para la alerta " + id));
    }

    private void cargarDatos(AlertaVital alerta, AlertaVitalRequest request) {
        alerta.setPacienteRut(request.getPacienteRut().trim());
        alerta.setPacienteNombre(request.getPacienteNombre().trim());
        alerta.setTipoSigno(request.getTipoSigno().trim().toUpperCase());
        alerta.setValor(request.getValor());
        alerta.setUnidad(request.getUnidad().trim());
        alerta.setUmbralMinimo(request.getUmbralMinimo());
        alerta.setUmbralMaximo(request.getUmbralMaximo());
        alerta.setObservacion(request.getObservacion());
    }

    private void aplicarEvaluacion(AlertaVital alerta) {
        double valor = alerta.getValor();
        double minimo = alerta.getUmbralMinimo();
        double maximo = alerta.getUmbralMaximo();

        if (valor < minimo) {
            alerta.setGravedad(calcularGravedad(valor, minimo, true));
            alerta.setMensaje("Valor bajo el umbral minimo para " + alerta.getTipoSigno());
        } else if (valor > maximo) {
            alerta.setGravedad(calcularGravedad(valor, maximo, false));
            alerta.setMensaje("Valor sobre el umbral maximo para " + alerta.getTipoSigno());
        } else {
            alerta.setGravedad("MEDIA");
            alerta.setMensaje("Senal vital dentro de rango, se mantiene en observacion");
        }
    }

    private String calcularGravedad(double valor, double umbral, boolean esMinimo) {
        if (esMinimo) {
            return valor <= umbral * 0.80 ? "CRITICA" : "ALTA";
        }
        return valor >= umbral * 1.20 ? "CRITICA" : "ALTA";
    }

    private void validarUmbrales(AlertaVitalRequest request) {
        if (request.getUmbralMinimo() >= request.getUmbralMaximo()) {
            throw new IllegalArgumentException("El umbral minimo debe ser menor que el umbral maximo");
        }
    }

    private AlertaVitalResponse toResponse(AlertaVital alerta) {
        AlertaVitalResponse response = new AlertaVitalResponse();
        response.setId(alerta.getId());
        response.setPacienteRut(alerta.getPacienteRut());
        response.setPacienteNombre(alerta.getPacienteNombre());
        response.setTipoSigno(alerta.getTipoSigno());
        response.setValor(alerta.getValor());
        response.setUnidad(alerta.getUnidad());
        response.setUmbralMinimo(alerta.getUmbralMinimo());
        response.setUmbralMaximo(alerta.getUmbralMaximo());
        response.setGravedad(alerta.getGravedad());
        response.setEstado(alerta.getEstado());
        response.setMensaje(alerta.getMensaje());
        response.setObservacion(alerta.getObservacion());
        response.setFechaRegistro(alerta.getFechaRegistro());
        response.setFechaActualizacion(alerta.getFechaActualizacion());
        return response;
    }
}
