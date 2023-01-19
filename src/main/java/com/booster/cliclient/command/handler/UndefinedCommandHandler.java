package com.booster.cliclient.command.handler;

import com.booster.cliclient.command.Command;
import com.booster.cliclient.console.OutputWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UndefinedCommandHandler implements CommandHandler {

    private final OutputWriter outputWriter;

    @Override
    public void handle() {
        outputWriter.println("Unsupported command");
    }

    @Override
    public Command command() {
        return Command.UNDEFINED;
    }

}
