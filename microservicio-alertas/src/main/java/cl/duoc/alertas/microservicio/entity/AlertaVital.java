package cl.duoc.alertas.microservicio.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "ALERTA_VITAL")
public class AlertaVital {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "alerta_vital_seq")
    @SequenceGenerator(name = "alerta_vital_seq", sequenceName = "ALERTA_VITAL_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "PACIENTE_RUT", nullable = false, length = 20)
    private String pacienteRut;

    @Column(name = "PACIENTE_NOMBRE", nullable = false, length = 120)
    private String pacienteNombre;

    @Column(name = "TIPO_SIGNO", nullable = false, length = 60)
    private String tipoSigno;

    @Column(name = "VALOR", nullable = false)
    private Double valor;

    @Column(name = "UNIDAD", nullable = false, length = 20)
    private String unidad;

    @Column(name = "UMBRAL_MINIMO", nullable = false)
    private Double umbralMinimo;

    @Column(name = "UMBRAL_MAXIMO", nullable = false)
    private Double umbralMaximo;

    @Column(name = "GRAVEDAD", nullable = false, length = 20)
    private String gravedad;

    @Column(name = "ESTADO", nullable = false, length = 20)
    private String estado;

    @Column(name = "MENSAJE", nullable = false, length = 250)
    private String mensaje;

    @Column(name = "OBSERVACION", length = 500)
    private String observacion;

    @Column(name = "FECHA_REGISTRO", nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "FECHA_ACTUALIZACION", nullable = false)
    private LocalDateTime fechaActualizacion;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.fechaRegistro = now;
        this.fechaActualizacion = now;
        if (this.estado == null || this.estado.isBlank()) {
            this.estado = "NUEVA";
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

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
