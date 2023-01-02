package com.booster.cliclient;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultipleAnswer<T> implements Answer<T> {

    private final List<Answer<T>> answers;

    public MultipleAnswer(Answer<T>... answer) {
        answers = new ArrayList<>(Arrays.asList(answer));
    }

    @Override
    public T answer(InvocationOnMock invocation) throws Throwable {
        return answers.remove(0).answer(invocation);
    }

}
