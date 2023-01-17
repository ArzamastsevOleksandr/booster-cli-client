package com.booster.cliclient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BoosterCliLauncher {

    private final UserInputReader adapter;
    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;
    private final OutputWriter outputWriter;

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
                    addVocabularyEntry();
                    break;
                case LIST_VOCABULARY_ENTRIES:
                    listVocabularyEntries();
                    break;
                case ADD_NOTE:
                    addNote();
                    break;
                case LIST_NOTES:
                    listNotes();
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

    @SneakyThrows
    // todo: handle OkHttp exceptions
    private void addVocabularyEntry() {
        outputWriter.print("Name");
        String name = adapter.readLine();

        Command cmd = Command.from(name);
        if (cmd == Command.EXIT) {
            return;
        }
        outputWriter.print("Description");
        String description = adapter.readLine();
        Command cmd2 = Command.from(description);
        if (cmd2 == Command.EXIT) {
            return;
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                objectMapper.writeValueAsString(new CreateVocabularyEntryInput()
                        .setName(name)
                        .setDescription(description)));

        Request request = new Request.Builder()
                .url("http://localhost:8081/vocabulary-entry")
                .post(requestBody)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                outputWriter.print("Error occurred");
            }
        }
    }

    @SneakyThrows
    // todo: handle OkHttp exceptions
    private void addNote() {
        outputWriter.print("Content");
        String content = adapter.readLine();

        Command cmd = Command.from(content);
        if (cmd == Command.EXIT) {
            return;
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                objectMapper.writeValueAsString(new CreateNoteInput().setContent(content)));

        Request request = new Request.Builder()
                .url("http://localhost:8081/note")
                .post(requestBody)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                outputWriter.print("Error occurred");
            }
        }
    }

    @SneakyThrows
    // todo: handle OkHttp exceptions
    private void listVocabularyEntries() {
        Request request = new Request.Builder()
                .url("http://localhost:8081/vocabulary-entry/list?size=5") // todo: temporary hardcode
                .get()
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String list = response.body().string();
                List<VocabularyEntryDto> vocabularyEntries = objectMapper.readValue(list, new TypeReference<>() {
                });
                vocabularyEntries.forEach(outputWriter::print);
            } else {
                outputWriter.print("Error occurred");
            }
        }
    }

    @SneakyThrows
    // todo: handle OkHttp exceptions
    private void listNotes() {
        Request request = new Request.Builder()
                .url("http://localhost:8081/note/list?size=5") // todo: temporary hardcode
                .get()
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String list = response.body().string();
                List<NoteDto> notes = objectMapper.readValue(list, new TypeReference<>() {
                });
                notes.forEach(outputWriter::print);
            } else {
                outputWriter.print("Error occurred");
            }
        }
    }

}
