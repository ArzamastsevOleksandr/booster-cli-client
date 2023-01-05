package com.booster.cliclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.StringBody.exact;

public class AddNoteCommandTest extends BaseIntegrationTest {

    @Test
    void addNoteCommandFlow() throws JsonProcessingException {
        String content = "My first note";

        mockServer.when(request().withMethod("POST").withPath("/note")
                        .withBody(exact(objectMapper.writeValueAsString(new CreateNoteInput().setContent(content)))))
                .respond(response().withStatusCode(201).withBody("""
                        {
                            "content": %s
                        }
                        """.formatted(content)));

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream console = System.out;
        try {
            System.setOut(new PrintStream(bytes));

            doAnswer(new MultipleAnswer<>(i -> {
                System.out.println(Command.ADD_NOTE.getValue());
                return Command.ADD_NOTE.getValue();
            }, i -> {
                System.out.println(content);
                return content;
            }, i -> {
                System.out.println(Command.EXIT.getValue());
                return Command.EXIT.getValue();
            })).when(adapter).readLine();

            boosterCliLauncher.start();
        } finally {
            System.setOut(console);
        }

        assertThat(bytes.toString().trim().stripIndent()).isEqualTo("""
                >> %s
                >> Content: %s
                >> %s""".formatted(Command.ADD_NOTE.getValue(),
                content,
                Command.EXIT.getValue()));
    }

}
