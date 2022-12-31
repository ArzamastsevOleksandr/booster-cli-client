package com.booster.cliclient;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.doAnswer;

@SpringBootTest
class BoosterCliClientApplicationTests {

    @Autowired
    BoosterCliLauncher boosterCliLauncher;
    @MockBean
    UserInputReader adapter;

    @Test
    void contextLoads() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream console = System.out;
        try {
            System.setOut(new PrintStream(bytes));

            doAnswer(new MultipleAnswer<>(i -> {
                System.out.println();
                return "";
            }, i -> {
                System.out.println("ave");
                return "ave";
            }, i -> {
                System.out.println("as");
                return "as";
            }, i -> {
                System.out.println("des");
                return "des";
            }, i -> {
                System.out.println("e");
                return "e";
            })).when(adapter).readLine();

            boosterCliLauncher.start();
        } finally {
            System.setOut(console);
        }
        assertThat(bytes.toString().trim().stripIndent()).isEqualTo("""
                >>
                >> ave
                >> Name: as
                >> Description: des
                Added vocabulary entry [name=as, description=des]
                >> e""".trim().stripIndent());
    }

    @RequiredArgsConstructor
    private static final class MultipleAnswer<T> implements Answer<T> {

        private final List<Answer<T>> answers;

        public MultipleAnswer(Answer<T>... answer) {
            answers = new ArrayList<>(Arrays.asList(answer));
        }

        @Override
        public T answer(InvocationOnMock invocation) throws Throwable {
            return answers.remove(0).answer(invocation);
        }
    }

}
