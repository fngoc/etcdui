package ru.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class EtcdUiApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(EtcdUiApplication.class, args);
    }

}
