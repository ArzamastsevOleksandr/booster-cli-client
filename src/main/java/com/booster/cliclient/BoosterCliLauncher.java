package com.booster.cliclient;

import com.booster.cliclient.handler.AddNoteHandler;
import com.booster.cliclient.handler.AddVocabularyEntryHandler;
import com.booster.cliclient.handler.ListNotesHandler;
import com.booster.cliclient.handler.ListVocabularyEntriesHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoosterCliLauncher {

    private final UserInputReader adapter;
    private final OutputWriter outputWriter;
    private final AddVocabularyEntryHandler addVocabularyEntryHandler;
    private final AddNoteHandler addNoteHandler;
    private final ListVocabularyEntriesHandler listVocabularyEntriesHandler;
    private final ListNotesHandler listNotesHandler;

    public void start() {
        Command command = getCommand();

        while (command != Command.EXIT) {
            switch (command) {
                case DO_NOTHING:
                    // do nothing
                    break;
                case UNDEFINED:
                    outputWriter.print("Unsupported command");
                    break;
                case ADD_VOCABULARY_ENTRY:
                    addVocabularyEntryHandler.handle();
                    break;
                case LIST_VOCABULARY_ENTRIES:
                    listVocabularyEntriesHandler.handle();
                    break;
                case ADD_NOTE:
                    addNoteHandler.handle();
                    break;
                case LIST_NOTES:
                    listNotesHandler.handle();
                    break;
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
