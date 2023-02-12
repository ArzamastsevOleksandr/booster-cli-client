package com.booster.cliclient.command.handler;

import com.booster.cliclient.command.Command;
import com.booster.cliclient.console.OutputWriter;
import com.booster.cliclient.console.UserInputReader;
import com.booster.cliclient.dto.CreateVocabularyEntryInput;
import com.booster.cliclient.dto.VocabularyEntryDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AddVocabularyEntryCommandHandler implements CommandHandler {

    private final UserInputReader adapter;
    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;
    private final OutputWriter outputWriter;

    @Override
    @SneakyThrows
    // todo: handle OkHttp exceptions
    public void handle() {
        outputWriter.print("Name");
        String name = adapter.readLine();

        if (Command.isExit(name)) {
            return;
        }
        outputWriter.print("Description");
        String description = adapter.readLine();
        if (Command.isExit(description)) {
            return;
        }
        outputWriter.println("Synonyms (one at a time). Type '%s' to break out or ENTER to finish".formatted(Command.EXIT.getValue()));
        Set<String> synonyms = new HashSet<>();
        String synonym = synonym();
        while (!Command.isExit(synonym) && Command.from(synonym) != Command.DO_NOTHING) {
            synonyms.add(synonym.strip());
            synonym = synonym();
        }
        if (Command.isExit(synonym)) {
            return;
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                objectMapper.writeValueAsString(new CreateVocabularyEntryInput()
                        .setName(name)
                        .setDescription(description)
                        .setSynonyms(synonyms)));

        Request request = new Request.Builder()
                .url("http://localhost:8081/vocabulary-entry")
                .post(requestBody)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                outputWriter.print("Error occurred [code=%s]".formatted(response.code()));
            } else {
                outputWriter.println(objectMapper.readValue(response.body().string(), VocabularyEntryDto.class));
                outputWriter.println();
            }
        }
    }

    private String synonym() {
        outputWriter.print("Synonym");
        return adapter.readLine();
    }

    @Override
    public Command command() {
        return Command.ADD_VOCABULARY_ENTRY;
    }

}
