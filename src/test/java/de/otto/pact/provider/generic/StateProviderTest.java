package de.otto.pact.provider.generic;

import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class StateProviderTest {

    private boolean called = false;

    public class TestStateProvider implements StateProvider {
        @State("some name")
        public void shouldDoStuff() {
            called = true;
        }
    }

    @Test
    public void shouldSetState() throws Exception {
        // Given
        called = false;
        final TestStateProvider testStateProvider = new TestStateProvider();

        // When
        testStateProvider.setState("some name");

        // Then
        assertThat(called, is(true));
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void shouldRaiseExceptionIfUndefinedState() throws Exception {
        new TestStateProvider().setState("some undefined name");
    }
}