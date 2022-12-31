package com.booster.cliclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BoosterCliClientApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BoosterCliClientApplication.class, args);

        context.getBean(BoosterCliLauncher.class).start();
    }

}
