package com.booster.cliclient.command.handler;

import com.booster.cliclient.command.Command;
import com.booster.cliclient.console.OutputWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class HelpCommandHandler implements CommandHandler {

    private final OutputWriter outputWriter;

    @Override
    public void handle() {
        outputWriter.println("The following commands are supported:");
        outputWriter.println();

        Arrays.stream(Command.values())
                .filter(c -> c != Command.DO_NOTHING && c != Command.UNDEFINED)
                .map(Command::toString)
                .forEach(outputWriter::println);

        outputWriter.println();

        outputWriter.println("To execute any command, type its short form, for example '%s' to list vocabulary entries"
                .formatted(Command.LIST_VOCABULARY_ENTRIES.getValue()));
        outputWriter.println();

        outputWriter.println("Type '%s' to stop the app or break out of other commands."
                .formatted(Command.EXIT.getValue()));
        outputWriter.println();
    }

    @Override
    public Command command() {
        return Command.HELP;
    }

}
