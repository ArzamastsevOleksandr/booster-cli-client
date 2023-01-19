package com.booster.cliclient.handler;

import com.booster.cliclient.Command;

public interface CommandHandler {

    void handle();

    Command command();

}
