package com.booster.cliclient.command.handler;

import com.booster.cliclient.command.Command;
import com.booster.cliclient.console.OutputWriter;
import com.booster.cliclient.console.UserInputReader;
import com.booster.cliclient.dto.CreateNoteInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddNoteCommandHandler implements CommandHandler {

    private final UserInputReader adapter;
    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;
    private final OutputWriter outputWriter;

    @Override
    @SneakyThrows
    // todo: handle OkHttp exceptions
    public void handle() {
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
                outputWriter.println("Error occurred");
            }
        }
    }

    @Override
    public Command command() {
        return Command.ADD_NOTE;
    }

}
