package com.booster.cliclient;

public class OutputFormatter {

    public static void printStart() {
        System.out.print(">> ");
    }

    public static void print(String outputMessage) {
        System.out.printf(">> %s: ", outputMessage);
    }

    public static void print(Object outputMessage) {
        System.out.printf(">> %s%n", outputMessage);
    }

    public static void print(String outputMessage, String inputParameter) {
        System.out.printf(">> %s: %s", outputMessage, inputParameter);
    }

}
