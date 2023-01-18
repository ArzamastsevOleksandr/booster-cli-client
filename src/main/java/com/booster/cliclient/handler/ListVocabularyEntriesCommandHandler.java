package com.booster.cliclient.handler;

import com.booster.cliclient.OutputWriter;
import com.booster.cliclient.VocabularyEntryDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListVocabularyEntriesCommandHandler implements CommandHandler {

    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;
    private final OutputWriter outputWriter;

    @Override
    @SneakyThrows
    // todo: handle OkHttp exceptions
    public void handle() {
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

}
