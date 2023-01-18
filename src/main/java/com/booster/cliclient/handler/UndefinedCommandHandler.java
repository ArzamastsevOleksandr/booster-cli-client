package com.booster.cliclient.handler;

import com.booster.cliclient.OutputWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UndefinedCommandHandler implements CommandHandler {

    private final OutputWriter outputWriter;

    @Override
    public void handle() {
        outputWriter.printWarning("Unsupported command");
    }

}
