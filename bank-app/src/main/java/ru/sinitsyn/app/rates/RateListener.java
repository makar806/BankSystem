package ru.sinitsyn.app.rates;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.sinitsyn.app.rates.DTO.RateMessage;
import ru.sinitsyn.service.impl.RateCacheService;
import ru.sinitsyn.service.model.RateModel;

@Component
public class RateListener {
    private final RateCacheService rateCacheService;
    private final ObjectMapper objectMapper;

    public RateListener(RateCacheService rateCacheService, ObjectMapper objectMapper) {
        this.rateCacheService = rateCacheService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "rates.updates.queue")
        public void handleRateUpdate(byte[] body) {
            try {
                RateMessage message = objectMapper.readValue(body, RateMessage.class);

                RateModel model = new RateModel(message.currency(), message.rateToRub(), message.timestamp());

                rateCacheService.saveRate(model);

                System.out.println("Received rate: " + message);
            } catch (Exception e) {
                throw new RuntimeException("can't pare rate message", e);
            }
    }


}

