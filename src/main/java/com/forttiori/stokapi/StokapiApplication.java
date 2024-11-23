package com.forttiori.stokapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StokapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(StokapiApplication.class, args);
    }

}
