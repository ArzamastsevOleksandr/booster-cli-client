package com.booster.cliclient.command.handler;

import com.booster.cliclient.command.Command;
import org.springframework.stereotype.Component;

@Component
public class DoNothingCommandHandler implements CommandHandler {

    @Override
    public void handle() {
        // do nothing
    }

    @Override
    public Command command() {
        return Command.DO_NOTHING;
    }

}
