package de.otto.pact.provider.generic;


import au.com.dius.pact.model.Consumer;
import au.com.dius.pact.model.Interaction;
import au.com.dius.pact.model.Pact;
import au.com.dius.pact.model.Provider;
import au.com.dius.pact.model.Request;
import au.com.dius.pact.model.Response;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import scala.Option;

import static com.google.common.collect.ImmutableList.of;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class PactEvaluatorTest {

    @Mock ProviderClient providerClientMock;
    @Mock StateProvider stateProviderMock;

    PactEvaluator subject;


    @BeforeMethod
    public void setupMocks() {
        initMocks(this);

        subject = new PactEvaluator(stateProviderMock,providerClientMock);
    }

    @Test
    public void shouldNotSetUpStateIfInteractionDidNotSpecifyState() {
        Request request = someRequest();
        Response expectedResponse = emptySuccessResponse();
        Pact pact = Pact.apply(Provider.apply("someProvider"), Consumer.apply("someConsumer"), of(Interaction.apply("someDescription", Option.empty(), request, expectedResponse)));

        when(providerClientMock.makeRequest(any(Request.class)))
                .thenReturn(expectedResponse);

        subject.evaluate(pact);

        verify(stateProviderMock,never()).setState(anyString());
    }

    @Test
    public void shouldSetUpStateIfSpecifiedInInteraction() {
        Request request = someRequest();
        Response expectedResponse = emptySuccessResponse();
        Pact pact = Pact.apply(Provider.apply("someProvider"), Consumer.apply("someConsumer"), of(Interaction.apply("someDescription", Option.apply("someState"), request, expectedResponse)));

        when(providerClientMock.makeRequest(any(Request.class)))
                .thenReturn(expectedResponse);

        subject.evaluate(pact);

        verify(stateProviderMock).setState("someState");
    }


    @Test
    public void shouldMakeGetRequest() {
        Request request = someRequest();
        Response expectedResponse = emptySuccessResponse();
        Pact pact = Pact.apply(Provider.apply("someProvider"), Consumer.apply("someConsumer"), of(Interaction.apply("someDescription", Option.empty(), request, expectedResponse)));

        when(providerClientMock.makeRequest(any(Request.class)))
                .thenReturn(expectedResponse);

        subject.evaluate(pact);

        verify(providerClientMock).makeRequest(request);
    }


    private Response emptySuccessResponse() {
        return Response.apply(200, Option.empty(), Option.empty(), Option.empty());
    }

    private Request someRequest() {
        return Request.apply("GET", "/some/path/", Option.empty(), Option.empty(), Option.empty(), Option.empty());
    }
}