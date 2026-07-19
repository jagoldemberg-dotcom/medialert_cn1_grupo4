package cl.duoc.medialert.bff.dto;

import java.time.Instant;

public record ColaResponse(String mensaje, String mensajeId, String cola, Instant publicadoEn) {
}
