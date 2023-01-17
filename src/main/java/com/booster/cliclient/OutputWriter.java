package com.booster.cliclient;

import org.springframework.stereotype.Service;

@Service
public class OutputWriter {

    public void printStart() {
        System.out.print(">> ");
    }

    public void print(String outputMessage) {
        System.out.printf(">> %s: ", outputMessage);
    }

    public void print(Object outputMessage) {
        System.out.printf(">> %s%n", outputMessage);
    }

}
