package cl.duoc.medialert.bff.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SenalVitalRequest(
        @NotBlank @Pattern(regexp = "^\\d{1,2}\\.\\d{3}\\.\\d{3}-[0-9Kk]$", message = "RUT inválido. Formato esperado 12.345.678-9") String pacienteRut,
        @NotBlank @Size(max = 120) String pacienteNombre,
        @NotBlank @Size(max = 60) String tipoSigno,
        @NotNull @DecimalMin(value = "0.0", inclusive = false) Double valor,
        @NotBlank @Size(max = 20) String unidad,
        @NotNull Double umbralMinimo,
        @NotNull Double umbralMaximo,
        @Size(max = 500) String observacion) {
}
