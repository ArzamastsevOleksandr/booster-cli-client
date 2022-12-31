package com.booster.cliclient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Configuration
public class BoosterCliConfig {

    @Bean
    UserInputReader userInputReader() {
        return new UserInputReader(new BufferedReader(new InputStreamReader(System.in)));
    }

}
