package ru.sinitsyn.rates;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RatesApplication {
    public static void main(String... args) {
        SpringApplication.run(RatesApplication.class, args);
    }
}
