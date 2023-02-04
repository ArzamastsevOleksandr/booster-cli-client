package com.booster.cliclient.command.service;

import com.booster.cliclient.command.Command;
import com.booster.cliclient.command.handler.CommandHandler;
import com.booster.cliclient.console.OutputWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommandHandlerService {

    private final List<CommandHandler> commandHandlers;
    private final OutputWriter outputWriter;

    private Map<Command, CommandHandler> commandHandlerPerCommand;

    @PostConstruct
    void registerCommandHandlers() {
        commandHandlerPerCommand = commandHandlers.stream()
                .collect(Collectors.toMap(CommandHandler::command, Function.identity()));
    }

    public void handle(Command command) {
        Optional.ofNullable(commandHandlerPerCommand.get(command))
                .ifPresentOrElse((commandHandler) -> {
                    try {
                        commandHandler.handle();
                    } catch (Exception e) {
                        outputWriter.println("Oops, an error occurred. Cause: %s".formatted(exceptionCauseOrErrorMessage(e)));
                    }
                }, () -> {
                    throw new IllegalArgumentException("Command has no handler [command=%s]".formatted(command));
                });
    }

    private String exceptionCauseOrErrorMessage(Exception e) {
        return Optional.of(e)
                .map(Exception::getCause)
                .map(Throwable::getMessage)
                .orElse(e.getMessage());
    }

}
