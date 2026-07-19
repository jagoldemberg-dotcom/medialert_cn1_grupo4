package cl.duoc.medialert.bff.service;

import cl.duoc.medialert.bff.dto.ColaResponse;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ColaService {
    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String signalKey;
    private final String summaryKey;

    public ColaService(RabbitTemplate rabbitTemplate,
                       @Value("${medialert.rabbit.exchange}") String exchange,
                       @Value("${medialert.rabbit.signal-routing-key}") String signalKey,
                       @Value("${medialert.rabbit.summary-routing-key}") String summaryKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.signalKey = signalKey;
        this.summaryKey = summaryKey;
    }

    public ColaResponse publicarSenal(Object request) {
        return publicar(request, signalKey, "cola.alertas.vitales", "Señal vital encolada");
    }

    public ColaResponse publicarResumen(Object request) {
        return publicar(request, summaryKey, "cola.resumen.vitales", "Resumen vital encolado");
    }

    private ColaResponse publicar(Object request, String routingKey, String queue, String message) {
        String messageId = UUID.randomUUID().toString();
        Instant now = Instant.now();
        Map<String, Object> envelope = new LinkedHashMap<>();
        envelope.put("mensajeId", messageId);
        envelope.put("publicadoEn", now.toString());
        envelope.put("datos", request);
        rabbitTemplate.convertAndSend(exchange, routingKey, envelope, m -> {
            m.getMessageProperties().setMessageId(messageId);
            m.getMessageProperties().setContentType("application/json");
            return m;
        });
        return new ColaResponse(message, messageId, queue, now);
    }
}
