package com.booster.cliclient.command.handler;

import com.booster.cliclient.command.Command;
import com.booster.cliclient.console.OutputWriter;
import com.booster.cliclient.console.UserInputReader;
import com.booster.cliclient.dto.VocabularyEntryDto;
import com.booster.cliclient.settings.SessionSettings;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListVocabularyEntriesCommandHandler implements CommandHandler {

    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;
    private final OutputWriter outputWriter;
    private final SessionSettings settings;
    private final UserInputReader reader;

    @Override
    @SneakyThrows
    // todo: handle OkHttp exceptions
    public void handle() {
        Request request = new Request.Builder()
                .url("http://localhost:8081/vocabulary-entry/list?size=100") // todo: temporary hardcode
                .get()
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String list = response.body().string();
                List<VocabularyEntryDto> vocabularyEntries = objectMapper.readValue(list, new TypeReference<>() {
                });
                int entriesCount = vocabularyEntries.size();
                String input = "breakOutOfTheLoop";
                for (int entryIndex = 0; entryIndex < entriesCount && !Command.isExit(input); ) {
                    int notesBatchSize = settings.vocabularyEntriesBatchSize();
                    for (int j = 0; j < notesBatchSize && entryIndex < entriesCount; ++j, ++entryIndex) {
                        outputWriter.print(vocabularyEntries.get(entryIndex));
                        if (entryIndex == entriesCount - 1) {
                            outputWriter.println();
                        } else if (j == notesBatchSize - 1) {
                            outputWriter.printStart();
                            input = reader.readLine();
                        }
                    }
                }
            } else {
                outputWriter.println("Error occurred");
            }
        }
    }

    @Override
    public Command command() {
        return Command.LIST_VOCABULARY_ENTRIES;
    }

}
