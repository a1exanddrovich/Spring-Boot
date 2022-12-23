package com.epam;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainEntryConsolePoint implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MainEntryConsolePoint.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("Hello, World!");
    }

}
