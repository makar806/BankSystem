package ru.sinitsyn.rates.service;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import ru.sinitsyn.rates.DTO.RateMessage;
import ru.sinitsyn.rates.config.RabbitConfig;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Random;

@Service
public class RatePublisher {
    private final RabbitTemplate rabbitTemplate;
    private final Random random = new Random();

    public RatePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(fixedDelayString = "${rates.publish-period-ms}")
    public void publishRates() {
        publishRate("USD", 90, 105);
        publishRate("EUR", 95, 115);
    }

    private void publishRate(String currency, int min, int max) {
        BigDecimal rate = BigDecimal.valueOf(min + random.nextDouble() * (max - min)).setScale(2, RoundingMode.HALF_UP);
        RateMessage message = new RateMessage(currency, rate, Instant.now());

        rabbitTemplate.convertAndSend(RabbitConfig.RATES_EXCHANGE, RabbitConfig.RATES_UPDATES_ROUTING_KEY, message);

        System.out.println("Published rate:" + message);
    }
}
