package ru.sinitsyn.rates.service;


import jakarta.annotation.PostConstruct;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import ru.sinitsyn.rates.DTO.RateMessage;
import ru.sinitsyn.rates.DTO.RateResponseMessage;
import ru.sinitsyn.rates.config.RabbitConfig;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RatePublisher {
    private final RabbitTemplate rabbitTemplate;
    private final Random random = new Random();
    private final Map<String, RateMessage> latestRates = new ConcurrentHashMap<>();

    public RatePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostConstruct
    public void initRates() {
        publishRates();
    }

    @Scheduled(fixedDelayString = "${rates.publish-period-ms}")
    public void publishRates() {
        publishRate("USD", 90, 105);
        publishRate("EUR", 95, 115);
    }

    private void publishRate(String currency, int min, int max) {
        BigDecimal rate = BigDecimal.valueOf(min + random.nextDouble() * (max - min)).setScale(2, RoundingMode.HALF_UP);
        RateMessage message = new RateMessage(currency, rate, Instant.now());

        latestRates.put(currency.toUpperCase(), message);

        rabbitTemplate.convertAndSend(RabbitConfig.RATES_EXCHANGE, RabbitConfig.RATES_UPDATES_ROUTING_KEY, message);

        System.out.println("Published rate:" + message);
    }

    @RabbitListener(queues = RabbitConfig.RATES_REQUEST_QUEUE)
    public RateResponseMessage handleRateRequest(RateResponseMessage request) {
        String normalizedCurrency = request.currency() == null
                ? ""
                : request.currency().trim().toUpperCase();

        if (!normalizedCurrency.matches("[A-Z]{3}")) {
            return new RateResponseMessage(normalizedCurrency, null, null, "Currency code must contain exactly 3 letters");

        }


        RateMessage message = latestRates.get(normalizedCurrency);

        if (message == null) {
            return new RateResponseMessage(normalizedCurrency, null,null, "Rate not found");
        }

        return new RateResponseMessage(message.currency(), message.rateToRub(),message.timestamp(),null);

    }
}
