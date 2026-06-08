package cl.duoc.alertas.bff.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AlertaVitalRequest {

    @NotBlank(message = "El RUT del paciente es obligatorio")
    @Pattern(regexp = "^\\d{1,2}\\.?\\d{3}\\.?\\d{3}-[0-9kK]$", message = "El RUT debe tener formato chileno valido, ejemplo 12.345.678-9")
    private String pacienteRut;

    @NotBlank(message = "El nombre del paciente es obligatorio")
    @Size(min = 3, max = 120, message = "El nombre debe tener entre 3 y 120 caracteres")
    private String pacienteNombre;

    @NotBlank(message = "El tipo de signo vital es obligatorio")
    private String tipoSigno;

    @NotNull(message = "El valor medido es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El valor debe ser mayor a cero")
    private Double valor;

    @NotBlank(message = "La unidad es obligatoria")
    private String unidad;

    @NotNull(message = "El umbral minimo es obligatorio")
    private Double umbralMinimo;

    @NotNull(message = "El umbral maximo es obligatorio")
    private Double umbralMaximo;

    private String observacion;

    public String getPacienteRut() { return pacienteRut; }
    public void setPacienteRut(String pacienteRut) { this.pacienteRut = pacienteRut; }
    public String getPacienteNombre() { return pacienteNombre; }
    public void setPacienteNombre(String pacienteNombre) { this.pacienteNombre = pacienteNombre; }
    public String getTipoSigno() { return tipoSigno; }
    public void setTipoSigno(String tipoSigno) { this.tipoSigno = tipoSigno; }
    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }
    public String getUnidad() { return unidad; }
    public void setUnidad(String unidad) { this.unidad = unidad; }
    public Double getUmbralMinimo() { return umbralMinimo; }
    public void setUmbralMinimo(Double umbralMinimo) { this.umbralMinimo = umbralMinimo; }
    public Double getUmbralMaximo() { return umbralMaximo; }
    public void setUmbralMaximo(Double umbralMaximo) { this.umbralMaximo = umbralMaximo; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}
