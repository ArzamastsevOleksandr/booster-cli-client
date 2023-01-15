package com.booster.cliclient;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class ListNotesTest extends BaseIntegrationTest {

    @Test
    void listNotesFlow() {
        String firstNoteContent = "Buy coffee";
        String secondNoteContent = "Call a friend";

        mockServer.when(request().withMethod("GET").withPath("/note/list")
                        .withQueryStringParameter("size", "5"))
                .respond(response().withStatusCode(200).withBody("""
                        [
                            {
                                "content": "%s"
                            },
                            {
                                "content": "%s"
                            }
                        ]
                        """.formatted(firstNoteContent, secondNoteContent)));

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream console = System.out;
        try {
            System.setOut(new PrintStream(bytes));

            doAnswer(new MultipleAnswer<>(i -> {
                System.out.println(Command.LIST_NOTES.getValue());
                return Command.LIST_NOTES.getValue();
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
                >> NoteDto(content=%s)
                >> NoteDto(content=%s)
                >> %s"""
                .formatted(Command.LIST_NOTES.getValue(),
                        firstNoteContent,
                        secondNoteContent,
                        Command.EXIT.getValue()));
    }

}
