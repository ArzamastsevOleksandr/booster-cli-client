package com.booster.cliclient;

import com.booster.cliclient.command.Command;
import com.booster.cliclient.settings.SessionSettings;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class ListNotesTest extends BaseIntegrationTest {

    @MockBean
    SessionSettings settings;

    @Test
    void listNotesFlow() {
        when(settings.notesBatchSize()).thenReturn(1);

        String firstNoteContent = "Buy coffee";
        String secondNoteContent = "Call a friend";

        mockServer.when(request().withMethod("GET").withPath("/note/list")
                        .withQueryStringParameter("size", "100"))
                .respond(response().withStatusCode(200).withBody("""
                        [
                            {
                                "id": 1,
                                "content": "%s"
                            },
                            {
                                "id": 2,
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
                >> %s
                >> NoteDto(id=1, content=%s)
                >>
                >> NoteDto(id=2, content=%s)
                >>
                >> %s"""
                .formatted(Command.HELP.getValue(),
                        Command.LIST_NOTES.getValue(),
                        firstNoteContent,
                        secondNoteContent,
                        Command.EXIT.getValue()));
    }

}
