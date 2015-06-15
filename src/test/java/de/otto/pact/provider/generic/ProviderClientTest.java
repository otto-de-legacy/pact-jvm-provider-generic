package de.otto.pact.provider.generic;

import au.com.dius.pact.model.Request;
import au.com.dius.pact.provider.PactConfiguration;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Param;
import org.testng.annotations.Test;
import scala.Option;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Test
public class ProviderClientTest {

    @Test
    public void shouldParseParams() throws Exception {
        // Given
        final ProviderClient providerClient = new ProviderClient(mock(PactConfiguration.class), mock(AsyncHttpClient.class));
        final String queryString = "q=someValue&some=otherValue";
        final Request request = mock(Request.class);
        final Option option = mock(Option.class);

        when(request.query()).thenReturn(option);
        when(option.isEmpty()).thenReturn(false);
        when(option.get()).thenReturn(queryString);

        // When
        final List<Param> params = providerClient.parseParams(request);

        // Then
        assertThat(params.size(), is(2));
        assertThat(params.get(0), is(new Param("q", "someValue")));
        assertThat(params.get(1), is(new Param("some", "otherValue")));
    }

    @Test
    public void shouldNotFailWithoutParams() throws Exception {
        // Given
        final ProviderClient providerClient = new ProviderClient(mock(PactConfiguration.class), mock(AsyncHttpClient.class));
        final Request request = mock(Request.class);
        final Option option = mock(Option.class);

        when(request.query()).thenReturn(option);
        when(option.isEmpty()).thenReturn(true);

        // When
        final List<Param> params = providerClient.parseParams(request);

        // Then
        assertThat(params, is(empty()));
    }
}
