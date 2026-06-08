package cl.duoc.alertas.microservicio.dto;

import java.time.LocalDateTime;

public class AlertaVitalResponse {
    private Long id;
    private String pacienteRut;
    private String pacienteNombre;
    private String tipoSigno;
    private Double valor;
    private String unidad;
    private Double umbralMinimo;
    private Double umbralMaximo;
    private String gravedad;
    private String estado;
    private String mensaje;
    private String observacion;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaActualizacion;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public String getGravedad() { return gravedad; }
    public void setGravedad(String gravedad) { this.gravedad = gravedad; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
