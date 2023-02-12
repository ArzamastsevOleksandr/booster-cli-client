package com.booster.cliclient;

import com.booster.cliclient.command.Command;
import com.booster.cliclient.dto.CreateVocabularyEntryInput;
import com.booster.cliclient.dto.VocabularyEntryDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

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
                                .setDescription(description)
                                .setSynonyms(List.of("unite", "combine"))))))
                .respond(response().withStatusCode(201).withBody(objectMapper.writeValueAsString(
                        new VocabularyEntryDto()
                                .setId(1L)
                                .setName(name)
                                .setDescription(description)
                                .setSynonyms(List.of("unite", "combine")))));

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream console = System.out;
        try {
            System.setOut(new PrintStream(bytes));

            doAnswer(new MultipleAnswer<>(i -> {
                System.out.println(Command.DO_NOTHING.getValue());
                return Command.DO_NOTHING.getValue();
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
                System.out.println("unite");
                return "unite";
            }, i -> {
                System.out.println("combine");
                return "combine";
            }, i -> {
                System.out.println(Command.DO_NOTHING.getValue());
                return Command.DO_NOTHING.getValue();
            }, i -> {
                System.out.println(Command.EXIT.getValue());
                return Command.EXIT.getValue();
            })).when(adapter).readLine();

            boosterCliLauncher.start();
        } finally {
            System.setOut(console);
        }
        assertThat(bytes.toString().trim().stripIndent()).isEqualTo("""
                >> Welcome to the booster-cli!
                >> Type any command or '%s' to get help.
                >>
                >>
                >> %s
                >> Name: %s
                >> Description: %s
                >> Synonyms (one at a time). Type '%s' to break out or ENTER to finish
                >> Synonym: %s
                >> Synonym: %s
                >> Synonym:
                >> VocabularyEntryDto(id=1, name=%s, description=%s, synonyms=[%s, %s])
                >>
                >> %s""".formatted(Command.HELP.getValue(),
                Command.ADD_VOCABULARY_ENTRY.getValue(),
                name,
                description,
                Command.EXIT.getValue(),
                "unite",
                "combine",
                name, description, "unite", "combine",
                Command.EXIT.getValue()));
    }

}
