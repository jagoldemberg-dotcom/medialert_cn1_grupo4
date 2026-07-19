package cl.duoc.medialert.alertas.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean Jackson2JsonMessageConverter jsonConverter(ObjectMapper mapper) { return new Jackson2JsonMessageConverter(mapper); }

    @Bean DirectExchange exchange(@Value("${medialert.rabbit.exchange}") String name) { return new DirectExchange(name, true, false); }
    @Bean DirectExchange deadLetterExchange() { return new DirectExchange("medialert.dlx", true, false); }

    @Bean Queue signalQueue(@Value("${medialert.rabbit.signal-queue}") String q) {
        return QueueBuilder.durable(q).deadLetterExchange("medialert.dlx").deadLetterRoutingKey("dlq.senales").build();
    }
    @Bean Queue signalDlq(@Value("${medialert.rabbit.signal-dlq}") String q) { return QueueBuilder.durable(q).build(); }
    @Bean Queue summaryQueue(@Value("${medialert.rabbit.summary-queue}") String q) {
        return QueueBuilder.durable(q).deadLetterExchange("medialert.dlx").deadLetterRoutingKey("dlq.resumenes").build();
    }
    @Bean Queue summaryDlq(@Value("${medialert.rabbit.summary-dlq}") String q) { return QueueBuilder.durable(q).build(); }

    @Bean Binding signalBinding(@Qualifier("signalQueue") Queue signalQueue, @Qualifier("exchange") DirectExchange exchange,
                                @Value("${medialert.rabbit.signal-routing-key}") String key) {
        return BindingBuilder.bind(signalQueue).to(exchange).with(key);
    }
    @Bean Binding summaryBinding(@Qualifier("summaryQueue") Queue summaryQueue, @Qualifier("exchange") DirectExchange exchange,
                                 @Value("${medialert.rabbit.summary-routing-key}") String key) {
        return BindingBuilder.bind(summaryQueue).to(exchange).with(key);
    }
    @Bean Binding signalDlqBinding(@Qualifier("signalDlq") Queue signalDlq, @Qualifier("deadLetterExchange") DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(signalDlq).to(deadLetterExchange).with("dlq.senales");
    }
    @Bean Binding summaryDlqBinding(@Qualifier("summaryDlq") Queue summaryDlq, @Qualifier("deadLetterExchange") DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(summaryDlq).to(deadLetterExchange).with("dlq.resumenes");
    }

    @Bean
    SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory cf,
                                                                        Jackson2JsonMessageConverter converter) {
        var factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(cf);
        factory.setMessageConverter(converter);
        factory.setDefaultRequeueRejected(false);
        return factory;
    }
}
