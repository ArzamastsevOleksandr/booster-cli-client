package com.booster.cliclient;

import com.booster.cliclient.handler.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BoosterCliLauncher {

    private static Map<Command, CommandHandler> commandHandlerMap;

    private final UserInputReader adapter;
    private final OutputWriter outputWriter;
    private final ApplicationContext applicationContext;

    @PostConstruct
    void registerCommandHandlers() {
        commandHandlerMap = Arrays.stream(applicationContext.getBeanNamesForType(CommandHandler.class))
                .map(name -> (CommandHandler) applicationContext.getBean(name))
                .collect(Collectors.toMap(CommandHandler::command, Function.identity()));
    }

    public void start() {
        Command command = getCommand();

        while (command != Command.EXIT) {
            commandHandlerMap.get(command).handle();
            command = getCommand();
        }
    }

    private Command getCommand() {
        outputWriter.printStart();
        String input = adapter.readLine();
        return Command.from(input);
    }

}
