package com.booster.cliclient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoosterCliLauncher {

    private final UserInputReader adapter;

    public void start() {
        System.out.print(">> ");
        String input = adapter.readLine();
        Command command = Command.from(input);

        while (command != Command.EXIT) {
            if (command == Command.DO_NOTHING) {
                // do nothing
            } else if (command == Command.UNDEFINED) {
                System.out.println("Unsupported command: " + input);
            } else if (command == Command.ADD_VOCABULARY_ENTRY) {
                addVocabularyEntry();
            }
            System.out.print(">> ");
            input = adapter.readLine();
            command = Command.from(input);
        }
    }

    private void addVocabularyEntry() {
        System.out.print(">> Name: ");
        String name = adapter.readLine();

        Command cmd = Command.from(name);
        if (cmd == Command.EXIT) {
            return;
        }
        System.out.print(">> Description: ");
        String description = adapter.readLine();
        Command cmd2 = Command.from(description);
        if (cmd2 == Command.EXIT) {
            return;
        }
        System.out.println("Added vocabulary entry [name=" + name + ", description=" + description + "]");
    }

}
