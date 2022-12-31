package com.booster.cliclient;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.BufferedReader;

@RequiredArgsConstructor
public class UserInputReader {

    private final BufferedReader bufferedReader;

    @SneakyThrows
    public String readLine() {
        return bufferedReader.readLine().strip();
    }

}
