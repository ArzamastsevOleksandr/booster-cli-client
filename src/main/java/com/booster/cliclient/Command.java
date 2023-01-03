package com.booster.cliclient;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum Command {

    EXIT("e"),
    DO_NOTHING(""),
    ADD_VOCABULARY_ENTRY("ave"),
    ADD_NOTE("an"),
    UNDEFINED("IGNORED");

    @Getter
    private final String value;

    public static Command from(String in) {
        return Arrays.stream(values())
                .filter(cmd -> cmd.value.equals(in))
                .findFirst()
                .orElse(UNDEFINED);
    }

}
