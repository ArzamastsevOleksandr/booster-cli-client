package com.booster.cliclient.command.handler;

import com.booster.cliclient.command.Command;
import com.booster.cliclient.dto.NoteDto;
import com.booster.cliclient.console.OutputWriter;
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
public class ListNotesCommandHandler implements CommandHandler {

    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;
    private final OutputWriter outputWriter;

    @Override
    @SneakyThrows
    // todo: handle OkHttp exceptions
    public void handle() {
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

    @Override
    public Command command() {
        return Command.LIST_NOTES;
    }

}
