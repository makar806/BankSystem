package ru.sinitsyn.service.impl;

import org.springframework.stereotype.Service;
import ru.sinitsyn.service.model.RateModel;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateCacheService {
    private final Map<String, RateModel> rates = new ConcurrentHashMap<>();

    public void saveRate(RateModel rate) {
        rates.put(rate.currency().toUpperCase(),rate);
    }

    public Optional<RateModel> getRate(String currency) {
        return Optional.ofNullable(rates.get(currency.toUpperCase()));
    }

    public Map<String, RateModel> getAllRates() {
        return Map.copyOf(rates);
    }


}
