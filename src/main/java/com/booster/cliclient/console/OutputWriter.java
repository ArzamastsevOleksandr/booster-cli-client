package com.booster.cliclient.console;

import org.springframework.stereotype.Service;

@Service
public class OutputWriter {

    public void printStart() {
        System.out.print(">> ");
    }

    public void print(String outputMessage) {
        System.out.printf(">> %s: ", outputMessage);
    }

    public void println(Object outputMessage) {
        System.out.printf(">> %s%n", outputMessage);
    }

    public void println() {
        System.out.printf(">>%n");
    }

    public void print(Object outputMessage) {
        System.out.printf(">> %s%n", outputMessage);
    }

}
