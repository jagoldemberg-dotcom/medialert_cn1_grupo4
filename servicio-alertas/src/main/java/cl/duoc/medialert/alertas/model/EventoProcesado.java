package cl.duoc.medialert.alertas.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "EVENTO_PROCESADO")
public class EventoProcesado {
    @Id @Column(length = 80) private String mensajeId;
    @Column(nullable = false, length = 30) private String tipo;
    @Column(nullable = false) private Instant procesadoEn;
    public EventoProcesado() {}
    public EventoProcesado(String mensajeId, String tipo, Instant procesadoEn) {
        this.mensajeId = mensajeId; this.tipo = tipo; this.procesadoEn = procesadoEn;
    }
    public String getMensajeId() { return mensajeId; }
    public void setMensajeId(String mensajeId) { this.mensajeId = mensajeId; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Instant getProcesadoEn() { return procesadoEn; }
    public void setProcesadoEn(Instant procesadoEn) { this.procesadoEn = procesadoEn; }
}
