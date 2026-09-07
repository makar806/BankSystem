package ru.sinitsyn.app;

import jakarta.persistence.Entity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.print.StreamPrintService;

@SpringBootApplication(scanBasePackages = "ru.sinitsyn")
@EntityScan(basePackages = "ru.sinitsyn.model")
public class Main {
    public static void main(String[] args){
        SpringApplication.run(Main.class, args);
    }
}