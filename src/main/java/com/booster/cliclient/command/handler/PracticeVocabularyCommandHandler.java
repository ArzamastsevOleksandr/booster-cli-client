package com.booster.cliclient.command.handler;

import com.booster.cliclient.command.Command;
import com.booster.cliclient.console.OutputWriter;
import com.booster.cliclient.console.UserInputReader;
import com.booster.cliclient.dto.UpdateCorrectAnswersCountInput;
import com.booster.cliclient.dto.VocabularyEntryDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Component
@RequiredArgsConstructor
public class PracticeVocabularyCommandHandler implements CommandHandler {

    private final UserInputReader adapter;
    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;
    private final OutputWriter outputWriter;

    @Override
    @SneakyThrows
    public void handle() {
        Request request = new Request.Builder()
                .url("http://localhost:8081/vocabulary-entry/list?size=100")
                .get()
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String list = response.body().string();
                List<VocabularyEntryDto> vocabularyEntries = objectMapper.readValue(list, new TypeReference<>() {
                });
                for (var vocabularyEntry : vocabularyEntries) {
                    if (vocabularyEntry.getSynonyms().isEmpty()) {
                        continue;
                    }
                    outputWriter.println("Word: " + vocabularyEntry.getName());
                    outputWriter.print("Synonyms");
                    String synonymsString = adapter.readLine();
                    if (Command.isExit(synonymsString)) {
                        outputWriter.println();
                        break;
                    }
                    Set<String> synonyms = Arrays.stream(synonymsString.split(";")).map(String::strip).collect(toSet());
                    Set<String> originSynonyms = new HashSet<>(vocabularyEntry.getSynonyms());
                    originSynonyms.removeAll(synonyms);
                    if (originSynonyms.isEmpty()) {
                        outputWriter.println("Correct");
                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                                objectMapper.writeValueAsString(new UpdateCorrectAnswersCountInput()
                                        .correct(true)
                                        .id(vocabularyEntry.getId())));

                        Request updateRequest = new Request.Builder()
                                .url("http://localhost:8081/vocabulary-entry/correct-answer-count")
                                .patch(requestBody)
                                .build();
                        Response updated = okHttpClient.newCall(updateRequest).execute();
                        if (!updated.isSuccessful()) {
                            outputWriter.println("Failed to increment the correct answers count");
                        }
                    } else {
                        outputWriter.println("Wrong. Original synonyms: " + vocabularyEntry.getSynonyms());
                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                                objectMapper.writeValueAsString(new UpdateCorrectAnswersCountInput()
                                        .correct(false)
                                        .id(vocabularyEntry.getId())));

                        Request updateRequest = new Request.Builder()
                                .url("http://localhost:8081/vocabulary-entry/correct-answer-count")
                                .patch(requestBody)
                                .build();
                        Response updated = okHttpClient.newCall(updateRequest).execute();
                        if (!updated.isSuccessful()) {
                            outputWriter.println("Failed to decrement the correct answers count");
                        }
                    }
                }
            } else {
                outputWriter.println("Error occurred");
            }
        }
    }

    @Override
    public Command command() {
        return Command.PRACTICE_VOCABULARY;
    }

}
