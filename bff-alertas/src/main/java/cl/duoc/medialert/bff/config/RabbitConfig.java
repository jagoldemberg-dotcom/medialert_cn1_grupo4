package cl.duoc.medialert.bff.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean Jackson2JsonMessageConverter jsonConverter(ObjectMapper mapper) { return new Jackson2JsonMessageConverter(mapper); }
    @Bean DirectExchange medialertExchange(@Value("${medialert.rabbit.exchange}") String name) { return new DirectExchange(name, true, false); }
    @Bean DirectExchange deadLetterExchange() { return new DirectExchange("medialert.dlx", true, false); }
    @Bean Queue signalQueue(@Value("${medialert.rabbit.signal-queue}") String name) { return QueueBuilder.durable(name).deadLetterExchange("medialert.dlx").deadLetterRoutingKey("dlq.senales").build(); }
    @Bean Queue signalDlq(@Value("${medialert.rabbit.signal-dlq}") String name) { return QueueBuilder.durable(name).build(); }
    @Bean Queue summaryQueue(@Value("${medialert.rabbit.summary-queue}") String name) { return QueueBuilder.durable(name).deadLetterExchange("medialert.dlx").deadLetterRoutingKey("dlq.resumenes").build(); }
    @Bean Queue summaryDlq(@Value("${medialert.rabbit.summary-dlq}") String name) { return QueueBuilder.durable(name).build(); }
    @Bean Binding signalBinding(@Qualifier("signalQueue") Queue queue, @Qualifier("medialertExchange") DirectExchange exchange, @Value("${medialert.rabbit.signal-routing-key}") String key) { return BindingBuilder.bind(queue).to(exchange).with(key); }
    @Bean Binding summaryBinding(@Qualifier("summaryQueue") Queue queue, @Qualifier("medialertExchange") DirectExchange exchange, @Value("${medialert.rabbit.summary-routing-key}") String key) { return BindingBuilder.bind(queue).to(exchange).with(key); }
    @Bean Binding signalDlqBinding(@Qualifier("signalDlq") Queue queue, @Qualifier("deadLetterExchange") DirectExchange exchange) { return BindingBuilder.bind(queue).to(exchange).with("dlq.senales"); }
    @Bean Binding summaryDlqBinding(@Qualifier("summaryDlq") Queue queue, @Qualifier("deadLetterExchange") DirectExchange exchange) { return BindingBuilder.bind(queue).to(exchange).with("dlq.resumenes"); }
}
