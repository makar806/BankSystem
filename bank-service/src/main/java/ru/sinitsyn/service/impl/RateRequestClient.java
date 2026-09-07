package ru.sinitsyn.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import ru.sinitsyn.service.exception.RateUnavailableException;
import ru.sinitsyn.service.model.RateModel;
import ru.sinitsyn.service.model.RateRequestMessage;
import ru.sinitsyn.service.model.RateResponseMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Service
public class RateRequestClient {
    private static final String RATES_REQUEST_QUEUE = "rates.request.queue";

    private final RabbitTemplate rabbitTemplate;

    public RateRequestClient(RabbitTemplate rabbitTemplate, @Value("${rates.request-timeout-ms:3000}") long requestTimeoutMs){
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setReplyTimeout(requestTimeoutMs);
    }

    public RateModel requestRate(String currency) {
        RateRequestMessage request = new RateRequestMessage(currency.trim().toUpperCase());

        RateResponseMessage response = rabbitTemplate.convertSendAndReceiveAsType(
                "",
                RATES_REQUEST_QUEUE,
                request,
                new ParameterizedTypeReference<RateResponseMessage>() {}
        );

        if (response == null) {
            throw new RateUnavailableException("Rates service is unavailable or request timed out");
        }

        if (response.error() != null) {
            throw new RateUnavailableException(response.error());
        }

        if (response.currency() == null || response.rateToRub() == null || response.timestamp() == null) {
            throw new RateUnavailableException("Invalid response from rates service");
        }

        return new RateModel(response.currency(), response.rateToRub(), response.timestamp());

}
}
