package com.booster.cliclient;

import com.booster.cliclient.command.Command;
import com.booster.cliclient.dto.UpdateCorrectAnswersCountInput;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.StringBody.exact;

class PracticeVocabularyCommandTest extends BaseIntegrationTest {

    @Test
    void practiceVocabularyCommandFlow() throws JsonProcessingException {
        String coalesce = "coalesce";
        String coalesceDescription = "come together to form one mass or whole";

        String robust = "robust";
        String robustDescription = "strong and healthy; hardy; vigorous";

        String renegade = "renegade";

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
                                "synonyms": ["hardy"]
                            },{
                                "id": 3,
                                "name": "%s",
                                "description": null,
                                "synonyms": ["traitor"]
                            }
                        ]
                        """.formatted(coalesce, coalesceDescription, robust, robustDescription, renegade)));

        mockServer.when(request().withMethod("PATCH").withPath("/vocabulary-entry/correct-answer-count")
                        .withBody(exact(objectMapper.writeValueAsString(new UpdateCorrectAnswersCountInput()
                                .correct(true)
                                .id(1L)))))
                .respond(response().withStatusCode(200));

        mockServer.when(request().withMethod("PATCH").withPath("/vocabulary-entry/correct-answer-count")
                        .withBody(exact(objectMapper.writeValueAsString(new UpdateCorrectAnswersCountInput()
                                .correct(true)
                                .id(2L)))))
                .respond(response().withStatusCode(200));

        mockServer.when(request().withMethod("PATCH").withPath("/vocabulary-entry/correct-answer-count")
                        .withBody(exact(objectMapper.writeValueAsString(new UpdateCorrectAnswersCountInput()
                                .correct(false)
                                .id(3L)))))
                .respond(response().withStatusCode(200));

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream console = System.out;
        try {
            System.setOut(new PrintStream(bytes));

            doAnswer(new MultipleAnswer<>(i -> {
                System.out.println(Command.PRACTICE_VOCABULARY.getValue());
                return Command.PRACTICE_VOCABULARY.getValue();
            }, i -> {
                System.out.println("unite;combine");
                return "unite;combine";
            }, i -> {
                System.out.println("hardy");
                return "hardy";
            }, i -> {
                System.out.println("trai");
                return "trai";
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
                >> Word: %s
                >> Synonyms: %s
                >> Correct
                >> Word: %s
                >> Synonyms: %s
                >> Correct
                >> Word: %s
                >> Synonyms: %s
                >> Wrong. Original synonyms: %s
                >> %s""".formatted(Command.HELP.getValue(),
                Command.PRACTICE_VOCABULARY.getValue(),
                coalesce,
                "unite;combine",
                robust,
                "hardy",
                renegade,
                "trai",
                Set.of("traitor"),
                Command.EXIT.getValue()));
    }

}
