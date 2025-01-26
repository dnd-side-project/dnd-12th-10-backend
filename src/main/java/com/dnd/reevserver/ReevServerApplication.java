package com.dnd.reevserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ReevServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReevServerApplication.class, args);
    }

}
