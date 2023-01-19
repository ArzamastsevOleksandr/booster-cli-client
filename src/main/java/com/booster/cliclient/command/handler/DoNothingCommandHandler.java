package com.booster.cliclient.command.handler;

import org.springframework.stereotype.Component;

@Component
public class DoNothingCommandHandler implements CommandHandler {

    @Override
    public void handle() {
        // do nothing
    }

}
