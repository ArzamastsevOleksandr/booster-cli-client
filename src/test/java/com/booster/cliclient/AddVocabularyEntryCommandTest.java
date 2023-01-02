package com.booster.cliclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.StringBody.exact;

class AddVocabularyEntryCommandTest extends BaseIntegrationTest {

    @Test
    void addVocabularyEntryCommandFlow() throws JsonProcessingException {
        String name = "coalesce";
        String description = "come together to form one mass or whole";

        mockServer.when(request().withMethod("POST").withPath("/vocabulary-entry")
                        .withBody(exact(objectMapper.writeValueAsString(new CreateVocabularyEntryInput()
                                .setName(name)
                                .setDescription(description)))))
                .respond(response().withStatusCode(201).withBody("""
                        {
                            "name": "%s",
                            "description": "%s"
                        }
                        """.formatted(name, description).trim().stripIndent()));

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream console = System.out;
        try {
            System.setOut(new PrintStream(bytes));

            doAnswer(new MultipleAnswer<>(i -> {
                System.out.println();
                return "";
            }, i -> {
                System.out.println(Command.ADD_VOCABULARY_ENTRY.getValue());
                return Command.ADD_VOCABULARY_ENTRY.getValue();
            }, i -> {
                System.out.println(name);
                return name;
            }, i -> {
                System.out.println(description);
                return description;
            }, i -> {
                System.out.println(Command.EXIT.getValue());
                return Command.EXIT.getValue();
            })).when(adapter).readLine();

            boosterCliLauncher.start();
        } finally {
            System.setOut(console);
        }
        assertThat(bytes.toString().trim().stripIndent()).isEqualTo("""
                >>
                >> %s
                >> Name: %s
                >> Description: %s
                >> Added vocabulary entry [name=%s, description=%s]
                >> %s""".formatted(Command.ADD_VOCABULARY_ENTRY.getValue(),
                name,
                description,
                name, description,
                Command.EXIT.getValue()).trim().stripIndent());
    }

}
