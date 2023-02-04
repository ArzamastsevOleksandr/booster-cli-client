package com.booster.cliclient.launcher;

import com.booster.cliclient.command.Command;
import com.booster.cliclient.command.service.CommandHandlerService;
import com.booster.cliclient.console.OutputWriter;
import com.booster.cliclient.console.UserInputReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoosterCliLauncher {

    private final UserInputReader adapter;
    private final OutputWriter outputWriter;
    private final CommandHandlerService commandHandlerService;

    public void start() {
        outputWriter.println("Welcome to the booster-cli!");
        outputWriter.println("Type any command or '%s' to get help.".formatted(Command.HELP.getValue()));
        outputWriter.println();

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
