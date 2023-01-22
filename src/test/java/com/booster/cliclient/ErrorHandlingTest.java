package com.booster.cliclient;

import com.booster.cliclient.command.Command;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ConnectException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;

@ExtendWith(MockitoExtension.class)
public class ErrorHandlingTest extends BaseIntegrationTest {

    @MockBean
    OkHttpClient okHttpClient;
    @Mock
    Call call;

    @Test
    void handleConnectExceptionWithoutAppCrashing() throws IOException {
        String content = "Check mailbox";

        given(okHttpClient.newCall(any())).willReturn(call);
        given(call.execute()).willThrow(new ConnectException().initCause(new Throwable("Connection refused")));

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
                >> Welcome to the booster-cli!
                >> Type any command or '%s' to get help.
                >> %s
                >> Content: %s
                >> Oops... We have some problems. Let us know and try a little bit later
                >> Cause: Connection refused
                >> %s""".formatted(Command.HELP.getValue(),
                Command.ADD_NOTE.getValue(),
                content,
                Command.EXIT.getValue()));
    }

}
