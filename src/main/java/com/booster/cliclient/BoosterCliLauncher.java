package com.booster.cliclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
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

        while (command != Command.EXIT) {
            if (command == Command.DO_NOTHING) {
                // do nothing
            } else if (command == Command.UNDEFINED) {
                System.out.println("Unsupported command: " + input);
            } else if (command == Command.ADD_VOCABULARY_ENTRY) {
                addVocabularyEntry();
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

}
