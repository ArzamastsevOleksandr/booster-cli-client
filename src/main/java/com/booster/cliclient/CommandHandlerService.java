package com.booster.cliclient;

import com.booster.cliclient.handler.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommandHandlerService {

    private final List<CommandHandler> commandHandlers;

    private Map<Command, CommandHandler> commandHandlerMap;

    @PostConstruct
    void registerCommandHandlers() {
        commandHandlerMap = commandHandlers.stream()
                .collect(Collectors.toMap(CommandHandler::command, Function.identity()));
    }

    public void handle(Command command) {
        commandHandlerMap.get(command).handle();
    }

}
