package cl.duoc.medialert.bff.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ResumenVitalRequest(
        @NotBlank @Pattern(regexp = "^\\d{1,2}\\.\\d{3}\\.\\d{3}-[0-9Kk]$") String pacienteRut,
        @NotBlank @Size(max = 120) String pacienteNombre,
        @NotNull @Min(1) @Max(1440) Integer periodoMinutos,
        @NotNull @Min(1) Integer cantidadMediciones,
        @NotNull @DecimalMin("20") @DecimalMax("250") Double promedioFrecuenciaCardiaca,
        @NotNull @DecimalMin("50") @DecimalMax("100") Double promedioSaturacionOxigeno,
        @Size(max = 500) String observacion) {
}
