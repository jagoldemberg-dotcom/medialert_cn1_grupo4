package cl.duoc.alertas.bff.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class EstadoAlertaUpdateRequest {
    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "NUEVA|ATENDIDA|DESCARTADA", message = "El estado permitido es NUEVA, ATENDIDA o DESCARTADA")
    private String estado;

    private String observacion;

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}
