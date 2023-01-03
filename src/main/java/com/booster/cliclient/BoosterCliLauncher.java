package com.booster.cliclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoosterCliLauncher {

    private final UserInputReader adapter;
    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;

    public void start() {
        System.out.print(">> ");
        String input = adapter.readLine();
        Command command = Command.from(input);

        while (command != Command.EXIT)
        {
            switch (command) {
                case DO_NOTHING:
                    // do nothing
                    break;
                case UNDEFINED:
                    System.out.println("Unsupported command: " + input);
                    break;
                case ADD_VOCABULARY_ENTRY:
                    addVocabularyEntry();
                    break;
                case ADD_NOTE:
                    addNote();
            }
            System.out.print(">> ");
            input = adapter.readLine();
            command = Command.from(input);
        }
    }

    @SneakyThrows
    // todo: handle OkHttp exceptions
    private void addVocabularyEntry() {
        System.out.print(">> Name: ");
        String name = adapter.readLine();

        Command cmd = Command.from(name);
        if (cmd == Command.EXIT) {
            return;
        }
        System.out.print(">> Description: ");
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
            if (response.isSuccessful()) {
                System.out.println(">> Added vocabulary entry [name=" + name + ", description=" + description + "]");
            } else {
                System.out.println(">> Error occurred");
            }
        }
    }

    @SneakyThrows
    // todo: handle OkHttp exceptions
    private void addNote() {
        System.out.print(">> Content: ");
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
            if (response.isSuccessful()) {
                System.out.println(">> Added note [content=" + content + "]");
            } else {
                System.out.println(">> Error occurred");
            }
        }
    }

}
