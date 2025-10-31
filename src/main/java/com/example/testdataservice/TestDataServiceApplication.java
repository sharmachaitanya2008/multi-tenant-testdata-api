package com.example.testdataservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TestDataServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestDataServiceApplication.class, args);
    }
}
