package cl.duoc.medialert.alertas.dto;
import java.time.Instant;
public record AlertaKafkaLegacy(Long pacienteId, String tipoAnomalia, String detalle, String severidad, Instant fechaHora) {}
