package com.booster.cliclient.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Command {

    EXIT("e", "exit"),
    DO_NOTHING("", "WRITE A COMPLAINT IF YOU SEE THIS:)"),
    ADD_VOCABULARY_ENTRY("ave", "add a vocabulary entry"),
    LIST_VOCABULARY_ENTRIES("lve", "list vocabulary entries"),
    ADD_NOTE("an", "add a note"),
    LIST_NOTES("ln", "list notes"),
    HELP("h", "help"),
    DELETE_NOTE("dn", "delete a note"),
    UNDEFINED("IGNORED", "WRITE A COMPLAINT IF YOU SEE THIS:)");

    private final String value;
    private final String userFriendlyName;

    public static Command from(String input) {
        return Arrays.stream(values())
                .filter(cmd -> cmd.value.equals(input))
                .findFirst()
                .orElse(UNDEFINED);
    }

    public static boolean isExit(String input) {
        return from(input) == EXIT;
    }

    @Override
    public String toString() {
        return getValue() + " - " + getUserFriendlyName();
    }

}
