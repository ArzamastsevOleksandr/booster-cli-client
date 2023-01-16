package com.booster.cliclient;

import org.springframework.stereotype.Service;

@Service
public class OutputFormatter {

    public void printStart() {
        System.out.print(">> ");
    }

    public void print(String outputMessage) {
        System.out.printf(">> %s: ", outputMessage);
    }

    public void print(Object outputMessage) {
        System.out.printf(">> %s%n", outputMessage);
    }

    public void print(String outputMessage, String inputParameter) {
        System.out.printf(">> %s: %s", outputMessage, inputParameter);
    }

}
