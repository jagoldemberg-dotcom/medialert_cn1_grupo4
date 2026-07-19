package cl.duoc.medialert;
import jakarta.validation.constraints.*;
import java.time.Instant;
public record SenalVital(
 @NotNull Long pacienteId,
 @Min(20) @Max(250) int frecuenciaCardiaca,
 @Min(50) @Max(260) int presionSistolica,
 @Min(30) @Max(180) int presionDiastolica,
 @DecimalMin("30") @DecimalMax("45") double temperatura,
 Instant fechaHora) {
 public SenalVital conFecha(){ return new SenalVital(pacienteId,frecuenciaCardiaca,presionSistolica,presionDiastolica,temperatura,fechaHora==null?Instant.now():fechaHora); }
}
