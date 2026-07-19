package cl.duoc.medialert.alertas.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "ALERTA_VITAL", indexes = {
        @Index(name = "IDX_ALERTA_ESTADO", columnList = "estado"),
        @Index(name = "IDX_ALERTA_PACIENTE", columnList = "pacienteRut")
})
public class Alerta {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "alerta_vital_seq")
    @SequenceGenerator(name = "alerta_vital_seq", sequenceName = "ALERTA_VITAL_SEQ", allocationSize = 1)
    private Long id;
    @Column(unique = true, length = 80) private String mensajeId;
    private Long pacienteId;
    @Column(nullable = false, length = 20) private String pacienteRut;
    @Column(nullable = false, length = 120) private String pacienteNombre;
    @Column(nullable = false, length = 60) private String tipoSigno;
    private Double valor;
    @Column(length = 20) private String unidad;
    private Double umbralMinimo;
    private Double umbralMaximo;
    @Column(nullable = false, length = 80) private String tipoAnomalia;
    @Column(nullable = false, length = 500) private String detalle;
    @Column(nullable = false, length = 20) private String severidad;
    @Column(nullable = false) private Instant fechaHora;
    @Column(nullable = false, length = 20) private String estado = "NUEVA";

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMensajeId() { return mensajeId; }
    public void setMensajeId(String mensajeId) { this.mensajeId = mensajeId; }
    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }
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
    public String getTipoAnomalia() { return tipoAnomalia; }
    public void setTipoAnomalia(String tipoAnomalia) { this.tipoAnomalia = tipoAnomalia; }
    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }
    public String getSeveridad() { return severidad; }
    public void setSeveridad(String severidad) { this.severidad = severidad; }
    public Instant getFechaHora() { return fechaHora; }
    public void setFechaHora(Instant fechaHora) { this.fechaHora = fechaHora; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
