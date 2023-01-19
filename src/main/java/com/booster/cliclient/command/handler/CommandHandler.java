package com.booster.cliclient.command.handler;

import com.booster.cliclient.Command;

public interface CommandHandler {

    void handle();

    Command command();

}
