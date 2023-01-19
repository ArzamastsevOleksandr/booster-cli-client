package com.booster.cliclient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoosterCliLauncher {

    private final UserInputReader adapter;
    private final OutputWriter outputWriter;
    private final CommandHandlerService commandHandlerService;

    public void start() {
        Command command = getCommand();

        while (command != Command.EXIT) {
            commandHandlerService.handle(command);
            command = getCommand();
        }
    }

    private Command getCommand() {
        outputWriter.printStart();
        String input = adapter.readLine();
        return Command.from(input);
    }

}
