package com.booster.cliclient.launcher;

import com.booster.cliclient.command.Command;
import com.booster.cliclient.command.handler.AddNoteCommandHandler;
import com.booster.cliclient.command.handler.AddVocabularyEntryCommandHandler;
import com.booster.cliclient.command.handler.DoNothingCommandHandler;
import com.booster.cliclient.command.handler.HelpCommandHandler;
import com.booster.cliclient.command.handler.ListNotesCommandHandler;
import com.booster.cliclient.command.handler.ListVocabularyEntriesCommandHandler;
import com.booster.cliclient.command.handler.UndefinedCommandHandler;
import com.booster.cliclient.console.OutputWriter;
import com.booster.cliclient.console.UserInputReader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoosterCliLauncher {

    private final UserInputReader adapter;
    private final OutputWriter outputWriter;
    private final AddVocabularyEntryCommandHandler addVocabularyEntryCommandHandler;
    private final AddNoteCommandHandler addNoteCommandHandler;
    private final ListVocabularyEntriesCommandHandler listVocabularyEntriesCommandHandler;
    private final ListNotesCommandHandler listNotesCommandHandler;
    private final DoNothingCommandHandler doNothingCommandHandler;
    private final UndefinedCommandHandler undefinedCommandHandler;
    private final HelpCommandHandler helpCommandHandler;

    public void start() {
        outputWriter.println("Welcome to the booster-cli!");
        outputWriter.println("Type any command or '%s' to get help.".formatted(Command.HELP.getValue()));

        Command command = getCommand();

        while (command != Command.EXIT) {
            switch (command) {
                case DO_NOTHING -> doNothingCommandHandler.handle();
                case UNDEFINED -> undefinedCommandHandler.handle();
                case ADD_VOCABULARY_ENTRY -> addVocabularyEntryCommandHandler.handle();
                case LIST_VOCABULARY_ENTRIES -> listVocabularyEntriesCommandHandler.handle();
                case ADD_NOTE -> addNoteCommandHandler.handle();
                case LIST_NOTES -> listNotesCommandHandler.handle();
                case HELP -> helpCommandHandler.handle();
                default -> throw new IllegalArgumentException("Command has no handler [command=%s]".formatted(command));
            }
            command = getCommand();
        }
    }

    private Command getCommand() {
        outputWriter.printStart();
        String input = adapter.readLine();
        return Command.from(input);
    }

}
