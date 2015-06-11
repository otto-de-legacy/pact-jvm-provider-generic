package de.otto.pact.provider.generic;

import au.com.dius.pact.model.Request;
import au.com.dius.pact.model.Response;
import au.com.dius.pact.provider.PactConfiguration;
import com.google.common.collect.ImmutableMap;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.FluentCaseInsensitiveStringsMap;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static de.otto.pact.provider.generic.util.Collectors.toImmutableMap;

public class ProviderClient {

    private final PactConfiguration config;
    private final AsyncHttpClient httpClient;

    public ProviderClient(PactConfiguration config, AsyncHttpClient httpClient) {
        this.config = config;
        this.httpClient = httpClient;
    }

    public Response makeRequest(Request request) {
        if ("GET".equals(request.method())) {
            String url = config.providerRoot().url() + request.path();
            try {
                com.ning.http.client.Response response = httpClient
                        .prepareGet(url)
                        .execute()
                        .get();
                return Response.apply(
                        response.getStatusCode(),
                        convertHeaders(response.getHeaders()),
                        response.getResponseBody(),
                        noResponseMatchingRules());
            } catch (InterruptedException | ExecutionException | IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("HTTP Method " + request.method()+" is not implemented yet");
        }
    }

    private ImmutableMap<String, Object> noResponseMatchingRules() {
        return ImmutableMap.of();
    }

    private Map<String, String> convertHeaders(FluentCaseInsensitiveStringsMap headers) {
        return headers.entrySet().stream()
                .collect(toImmutableMap(Map.Entry::getKey, e -> e.getValue().get(0)));

    }
}