package com.booster.cliclient;

import com.booster.cliclient.command.Command;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.doAnswer;

public class HelpCommandTest extends BaseIntegrationTest {

    @Test
    void helpCommandFlow() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream console = System.out;
        try {
            System.setOut(new PrintStream(bytes));

            doAnswer(new MultipleAnswer<>(i -> {
                System.out.println(Command.HELP.getValue());
                return Command.HELP.getValue();
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
                >> The following commands are supported:
                >>
                >> e - exit
                >> ave - add a vocabulary entry
                >> lve - list vocabulary entries
                >> an - add a note
                >> ln - list notes
                >> h - help
                >>
                >> To execute any command, type its short form, for example '%s' to list vocabulary entries
                >>
                >> Type 'e' to stop the app or break out of other commands.
                >>
                >> e""".formatted(Command.HELP.getValue(),
                Command.HELP.getValue(),
                Command.LIST_VOCABULARY_ENTRIES.getValue(),
                Command.EXIT.getValue(),
                Command.EXIT.getValue()));
    }

}
