package com.booster.cliclient.handler;

import com.booster.cliclient.Command;
import com.booster.cliclient.CreateNoteInput;
import com.booster.cliclient.OutputWriter;
import com.booster.cliclient.UserInputReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.*;
import org.springframework.stereotype.Service;

@Service
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
                outputWriter.print("Error occurred");
            }
        }
    }

}
