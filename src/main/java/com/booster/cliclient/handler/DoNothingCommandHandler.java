package com.booster.cliclient.handler;

import com.booster.cliclient.Command;
import org.springframework.stereotype.Service;

@Service
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
