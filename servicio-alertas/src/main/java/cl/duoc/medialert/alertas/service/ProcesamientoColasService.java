package cl.duoc.medialert.alertas.service;

import cl.duoc.medialert.alertas.dto.AlertaKafkaLegacy;
import cl.duoc.medialert.alertas.model.*;
import cl.duoc.medialert.alertas.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProcesamientoColasService {
    private final ObjectMapper mapper;
    private final AlertaRepository alertas;
    private final ResumenVitalRepository resumenes;
    private final EventoProcesadoRepository procesados;
    private final KafkaTemplate<String, Object> kafka;
    private final String alertTopic;
    private final String summaryTopic;

    public ProcesamientoColasService(ObjectMapper mapper, AlertaRepository alertas,
            ResumenVitalRepository resumenes, EventoProcesadoRepository procesados,
            KafkaTemplate<String, Object> kafka,
            @Value("${medialert.kafka.alert-topic}") String alertTopic,
            @Value("${medialert.kafka.summary-topic}") String summaryTopic) {
        this.mapper = mapper; this.alertas = alertas; this.resumenes = resumenes;
        this.procesados = procesados; this.kafka = kafka; this.alertTopic = alertTopic; this.summaryTopic = summaryTopic;
    }

    @RabbitListener(queues = "${medialert.rabbit.signal-queue}")
    @Transactional
    public void consumirSenal(Map<String, Object> envelope) {
        String id = Objects.toString(envelope.get("mensajeId"), "");
        if (id.isBlank() || procesados.existsById(id)) return;
        Map<String, Object> d = mapper.convertValue(envelope.get("datos"), Map.class);
        double value = number(d.get("valor"));
        double min = number(d.get("umbralMinimo"));
        double max = number(d.get("umbralMaximo"));
        String severity = clasificar(value, min, max);

        Alerta a = new Alerta();
        a.setMensajeId(id);
        a.setPacienteRut(text(d, "pacienteRut", "SIN-RUT"));
        a.setPacienteNombre(text(d, "pacienteNombre", "Paciente"));
        a.setTipoSigno(text(d, "tipoSigno", "SIGNO_VITAL"));
        a.setValor(value); a.setUnidad(text(d, "unidad", "")); a.setUmbralMinimo(min); a.setUmbralMaximo(max);
        a.setTipoAnomalia(value < min ? "VALOR_BAJO" : value > max ? "VALOR_ALTO" : "VALOR_NORMAL");
        a.setDetalle("Valor " + value + " " + a.getUnidad() + "; rango esperado " + min + " a " + max + ". " + text(d, "observacion", ""));
        a.setSeveridad(severity); a.setEstado("NUEVA"); a.setFechaHora(Instant.now());
        alertas.save(a);
        procesados.save(new EventoProcesado(id, "SENAL", Instant.now()));
        kafka.send(alertTopic, a.getPacienteRut(), eventoAlerta(a));
    }

    @RabbitListener(queues = "${medialert.rabbit.summary-queue}")
    @Transactional
    public void consumirResumen(Map<String, Object> envelope) {
        String id = Objects.toString(envelope.get("mensajeId"), "");
        if (id.isBlank() || procesados.existsById(id)) return;
        Map<String, Object> d = mapper.convertValue(envelope.get("datos"), Map.class);
        double fc = number(d.get("promedioFrecuenciaCardiaca"));
        double spo2 = number(d.get("promedioSaturacionOxigeno"));
        String classification = (fc > 120 || spo2 < 90) ? "CRITICA" : (fc > 100 || spo2 < 94) ? "ALTA" : "NORMAL";
        ResumenVital r = new ResumenVital();
        r.setMensajeId(id); r.setPacienteRut(text(d, "pacienteRut", "SIN-RUT")); r.setPacienteNombre(text(d, "pacienteNombre", "Paciente"));
        r.setPeriodoMinutos(integer(d.get("periodoMinutos"))); r.setCantidadMediciones(integer(d.get("cantidadMediciones")));
        r.setPromedioFrecuenciaCardiaca(fc); r.setPromedioSaturacionOxigeno(spo2); r.setClasificacion(classification);
        r.setObservacion(text(d, "observacion", "")); r.setFechaHora(Instant.now());
        resumenes.save(r);
        procesados.save(new EventoProcesado(id, "RESUMEN", Instant.now()));
        kafka.send(summaryTopic, r.getPacienteRut(), eventoResumen(r));
    }

    @KafkaListener(topics = "alertas", groupId = "servicio-alertas-final")
    @Transactional
    public void consumirAlertaExperiencia3(AlertaKafkaLegacy e) {
        String id = "kafka-s8-" + UUID.randomUUID();
        Alerta a = new Alerta();
        a.setMensajeId(id); a.setPacienteId(e.pacienteId()); a.setPacienteRut("PAC-" + e.pacienteId());
        a.setPacienteNombre("Paciente " + e.pacienteId()); a.setTipoSigno("SIGNOS_VITALES");
        a.setTipoAnomalia(e.tipoAnomalia()); a.setDetalle(e.detalle()); a.setSeveridad(e.severidad());
        a.setFechaHora(e.fechaHora() == null ? Instant.now() : e.fechaHora()); a.setEstado("NUEVA");
        alertas.save(a); procesados.save(new EventoProcesado(id, "KAFKA_S8", Instant.now()));
        kafka.send(alertTopic, a.getPacienteRut(), eventoAlerta(a));
    }

    private Map<String, Object> eventoAlerta(Alerta a) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("tipoEvento", "ALERTA"); m.put("mensajeId", a.getMensajeId()); m.put("alertaId", a.getId());
        m.put("pacienteRut", a.getPacienteRut()); m.put("pacienteNombre", a.getPacienteNombre());
        m.put("tipoSigno", a.getTipoSigno()); m.put("valor", a.getValor()); m.put("unidad", a.getUnidad());
        m.put("severidad", a.getSeveridad()); m.put("estado", a.getEstado()); m.put("detalle", a.getDetalle());
        m.put("fechaHora", a.getFechaHora().toString()); return m;
    }
    private Map<String, Object> eventoResumen(ResumenVital r) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("tipoEvento", "RESUMEN"); m.put("mensajeId", r.getMensajeId()); m.put("resumenId", r.getId());
        m.put("pacienteRut", r.getPacienteRut()); m.put("pacienteNombre", r.getPacienteNombre());
        m.put("promedioFrecuenciaCardiaca", r.getPromedioFrecuenciaCardiaca());
        m.put("promedioSaturacionOxigeno", r.getPromedioSaturacionOxigeno());
        m.put("severidad", r.getClasificacion()); m.put("fechaHora", r.getFechaHora().toString()); return m;
    }
    private String clasificar(double v, double min, double max) {
        if (v >= min && v <= max) return "NORMAL";
        double range = Math.max(1, max - min);
        double distance = v < min ? min - v : v - max;
        return distance > range * 0.35 ? "CRITICA" : "ALTA";
    }
    private String text(Map<String, Object> m, String key, String def) { Object v=m.get(key); return v==null?def:String.valueOf(v); }
    private double number(Object v) { return v instanceof Number n ? n.doubleValue() : Double.parseDouble(String.valueOf(v)); }
    private int integer(Object v) { return v instanceof Number n ? n.intValue() : Integer.parseInt(String.valueOf(v)); }
}
