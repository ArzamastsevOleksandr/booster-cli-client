package com.booster.cliclient;

import com.booster.cliclient.handler.*;
import lombok.RequiredArgsConstructor;
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

    public void start() {
        Command command = getCommand();

        while (command != Command.EXIT) {
            switch (command) {
                case DO_NOTHING -> doNothingCommandHandler.handle();
                case UNDEFINED -> undefinedCommandHandler.handle();
                case ADD_VOCABULARY_ENTRY -> addVocabularyEntryCommandHandler.handle();
                case LIST_VOCABULARY_ENTRIES -> listVocabularyEntriesCommandHandler.handle();
                case ADD_NOTE -> addNoteCommandHandler.handle();
                case LIST_NOTES -> listNotesCommandHandler.handle();
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
