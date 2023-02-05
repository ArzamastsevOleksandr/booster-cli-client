package com.booster.cliclient;

import com.booster.cliclient.command.Command;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class DeleteNoteCommandTest extends BaseIntegrationTest {

    @Test
    void deleteNoteCommandFlow() {
        mockServer.when(request().withMethod("DELETE").withPath("/note/1"))
                .respond(response().withStatusCode(204));

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream console = System.out;
        try {
            System.setOut(new PrintStream(bytes));

            doAnswer(new MultipleAnswer<>(i -> {
                System.out.println(Command.DELETE_NOTE.getValue());
                return Command.DELETE_NOTE.getValue();
            }, i -> {
                System.out.println("1");
                return "1";
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
                >> %s
                >> Id: 1
                >> Note deleted
                >> %s""".formatted(Command.HELP.getValue(),
                Command.DELETE_NOTE.getValue(),
                Command.EXIT.getValue()));
    }

}
