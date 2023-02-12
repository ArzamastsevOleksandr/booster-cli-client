package com.booster.cliclient.command.handler;

import com.booster.cliclient.command.Command;
import com.booster.cliclient.console.OutputWriter;
import com.booster.cliclient.console.UserInputReader;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteNoteCommandHandler implements CommandHandler {

    private final UserInputReader adapter;
    private final OkHttpClient okHttpClient;
    private final OutputWriter outputWriter;

    @Override
    @SneakyThrows
    public void handle() {
        outputWriter.print("Id");
        String id = adapter.readLine();

        Command cmd = Command.from(id);
        if (cmd == Command.EXIT) {
            return;
        }

        Request request = new Request.Builder()
                .url("http://localhost:8081/note/" + id)
                .delete()
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                outputWriter.println("Error occurred");
            } else {
                outputWriter.println("Note deleted");
            }
        }
    }

    @Override
    public Command command() {
        return Command.DELETE_NOTE;
    }

}
