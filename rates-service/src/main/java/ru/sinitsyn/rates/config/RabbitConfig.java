package ru.sinitsyn.rates.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String RATES_EXCHANGE = "rates.exchange";
    public static final String RATES_UPDATES_QUEUE = "rates.updates.queue";
    public static final String RATES_UPDATES_ROUTING_KEY = "rates.update";

    @Bean
    public TopicExchange rateExchange() {
        return new TopicExchange(RATES_EXCHANGE);
    }

    @Bean
    public Queue rateUpdatesQueue() {
        return new Queue(RATES_UPDATES_QUEUE);
    }

    @Bean
    public Binding ratesUpdatesBinding() {
        return BindingBuilder.bind(rateUpdatesQueue()).to(rateExchange()).with(RATES_UPDATES_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
