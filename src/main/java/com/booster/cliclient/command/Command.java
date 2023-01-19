package com.booster.cliclient.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum Command {

    EXIT("e"),
    DO_NOTHING(""),
    ADD_VOCABULARY_ENTRY("ave"),
    LIST_VOCABULARY_ENTRIES("lve"),
    ADD_NOTE("an"),
    LIST_NOTES("ln"),
    HELP("h"),
    UNDEFINED("IGNORED");

    @Getter
    private final String value;

    public static Command from(String in) {
        return Arrays.stream(values())
                .filter(cmd -> cmd.value.equals(in))
                .findFirst()
                .orElse(UNDEFINED);
    }

    @Override
    public String toString() {
        return this.name() + "(" + getValue() + ")";
    }

}
