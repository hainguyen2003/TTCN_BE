package com.example.ttcn2etest;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class Ttcn2ETestApplication    {

    public static void main(String[] args) {

        // Load .env file
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        // Set system properties from .env
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));


        SpringApplication.run(Ttcn2ETestApplication.class, args);
    }

}