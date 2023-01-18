package com.booster.cliclient.handler;

import org.springframework.stereotype.Service;

@Service
public class DoNothingCommandHandler implements CommandHandler {

    @Override
    public void handle() {
        // do nothing
    }

}
