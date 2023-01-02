package com.booster.cliclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
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

    @Bean
    OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
