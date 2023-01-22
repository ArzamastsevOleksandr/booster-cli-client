package com.booster.cliclient.command.handler;

import com.booster.cliclient.command.Command;
import com.booster.cliclient.console.OutputWriter;
import com.booster.cliclient.console.UserInputReader;
import com.booster.cliclient.dto.CreateVocabularyEntryInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.*;
import org.springframework.stereotype.Component;

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
        } catch (Exception e) {
            outputWriter.println("Oops... We have some problems. Let us know and try a little bit later");
            outputWriter.println("Cause: %s".formatted(e.getCause().getMessage()));
            // TODO: log trace/debug can be add for stack trace
        }
    }

    @Override
    public Command command() {
        return Command.ADD_VOCABULARY_ENTRY;
    }

}
