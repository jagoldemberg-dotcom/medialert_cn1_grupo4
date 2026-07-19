package cl.duoc.medialert.alertas.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "RESUMEN_VITAL")
public class ResumenVital {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resumen_vital_seq")
    @SequenceGenerator(name = "resumen_vital_seq", sequenceName = "RESUMEN_VITAL_SEQ", allocationSize = 1)
    private Long id;
    @Column(nullable = false, unique = true, length = 80) private String mensajeId;
    @Column(nullable = false, length = 20) private String pacienteRut;
    @Column(nullable = false, length = 120) private String pacienteNombre;
    private Integer periodoMinutos;
    private Integer cantidadMediciones;
    private Double promedioFrecuenciaCardiaca;
    private Double promedioSaturacionOxigeno;
    @Column(nullable = false, length = 20) private String clasificacion;
    @Column(length = 500) private String observacion;
    @Column(nullable = false) private Instant fechaHora;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMensajeId() { return mensajeId; }
    public void setMensajeId(String mensajeId) { this.mensajeId = mensajeId; }
    public String getPacienteRut() { return pacienteRut; }
    public void setPacienteRut(String pacienteRut) { this.pacienteRut = pacienteRut; }
    public String getPacienteNombre() { return pacienteNombre; }
    public void setPacienteNombre(String pacienteNombre) { this.pacienteNombre = pacienteNombre; }
    public Integer getPeriodoMinutos() { return periodoMinutos; }
    public void setPeriodoMinutos(Integer periodoMinutos) { this.periodoMinutos = periodoMinutos; }
    public Integer getCantidadMediciones() { return cantidadMediciones; }
    public void setCantidadMediciones(Integer cantidadMediciones) { this.cantidadMediciones = cantidadMediciones; }
    public Double getPromedioFrecuenciaCardiaca() { return promedioFrecuenciaCardiaca; }
    public void setPromedioFrecuenciaCardiaca(Double valor) { this.promedioFrecuenciaCardiaca = valor; }
    public Double getPromedioSaturacionOxigeno() { return promedioSaturacionOxigeno; }
    public void setPromedioSaturacionOxigeno(Double valor) { this.promedioSaturacionOxigeno = valor; }
    public String getClasificacion() { return clasificacion; }
    public void setClasificacion(String clasificacion) { this.clasificacion = clasificacion; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
    public Instant getFechaHora() { return fechaHora; }
    public void setFechaHora(Instant fechaHora) { this.fechaHora = fechaHora; }
}
