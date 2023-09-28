package com.festago.support;

import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

public class SetUpMockito {

    public static <T> Given<T> given(T methodCall) {
        OngoingStubbing<T> ongoingStubbing = Mockito.lenient().when(methodCall);
        return new Given<>(ongoingStubbing);
    }

    public static class Given<T> {

        private final OngoingStubbing<T> ongoingStubbing;

        public Given(OngoingStubbing<T> ongoingStubbing) {
            this.ongoingStubbing = ongoingStubbing;
        }

        public void willReturn(T value) {
            ongoingStubbing.thenReturn(value);
        }

        public void willAnswer(Answer<?> answer) {
            ongoingStubbing.thenAnswer(answer);
        }
    }
}
