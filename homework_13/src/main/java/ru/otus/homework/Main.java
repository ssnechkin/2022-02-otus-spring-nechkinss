package ru.otus.homework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
