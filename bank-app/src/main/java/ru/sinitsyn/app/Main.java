package ru.sinitsyn.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.print.StreamPrintService;

@SpringBootApplication(scanBasePackages = "ru.sinitsyn")
@EnableJpaRepositories(basePackages = "ru.sinitsyn.dao.repositories")
@EntityScan(basePackages = "ru.sinitsyn.model")
public class Main {
    public static void main(String[] args){
        SpringApplication.run(Main.class, args);
    }
}