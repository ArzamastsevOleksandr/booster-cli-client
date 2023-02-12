package com.booster.cliclient;

import com.booster.cliclient.command.Command;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class ListVocabularyEntriesTest extends BaseIntegrationTest {

    @Test
    void listVocabularyEntriesFlow() {
        String coalesce = "coalesce";
        String coalesceDescription = "come together to form one mass or whole";
        String robust = "robust";
        String robustDescription = "strong and healthy; hardy; vigorous";

        mockServer.when(request().withMethod("GET").withPath("/vocabulary-entry/list")
                        .withQueryStringParameter("size", "100"))
                .respond(response().withStatusCode(200).withBody("""
                        [
                            {
                                "id": 1,
                                "name": "%s",
                                "description": "%s",
                                "synonyms": ["unite", "combine"]
                            },
                            {
                                "id": 2,
                                "name": "%s",
                                "description": "%s",
                                "synonyms": []
                            }
                        ]
                        """.formatted(coalesce, coalesceDescription, robust, robustDescription)));

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream console = System.out;
        try {
            System.setOut(new PrintStream(bytes));

            doAnswer(new MultipleAnswer<>(i -> {
                System.out.println(Command.LIST_VOCABULARY_ENTRIES.getValue());
                return Command.LIST_VOCABULARY_ENTRIES.getValue();
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
                >> %s
                >> VocabularyEntryDto(id=1, name=%s, description=%s, synonyms=[%s, %s])
                >> VocabularyEntryDto(id=2, name=%s, description=%s, synonyms=[])
                >> %s"""
                .formatted(Command.HELP.getValue(),
                        Command.LIST_VOCABULARY_ENTRIES.getValue(),
                        coalesce, coalesceDescription,
                        "unite", "combine",
                        robust, robustDescription,
                        Command.EXIT.getValue()));
    }

}
