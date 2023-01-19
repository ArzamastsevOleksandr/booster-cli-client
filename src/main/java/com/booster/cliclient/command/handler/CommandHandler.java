package com.booster.cliclient.command.handler;

import com.booster.cliclient.command.Command;

public interface CommandHandler {

    void handle();

    Command command();

}
