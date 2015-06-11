package de.otto.pact.provider.generic;

import au.com.dius.pact.model.Interaction;
import au.com.dius.pact.model.Pact;
import au.com.dius.pact.model.Response;
import au.com.dius.pact.model.ResponseMatching$;
import au.com.dius.pact.model.ResponsePartMismatch;
import com.google.common.collect.ImmutableList;
import scala.collection.Seq;

import java.util.stream.Stream;

import static de.otto.pact.provider.generic.ScalaInterop.fromSeq;
import static de.otto.pact.provider.generic.util.Collectors.toImmutableList;


public class PactEvaluator {
    private static final ResponseMatching$ responseMatching = ResponseMatching$.MODULE$;

    private final StateProvider stateProvider;
    private final ProviderClient providerClient;


    public PactEvaluator(StateProvider stateProvider, ProviderClient providerClient) {
        this.stateProvider = stateProvider;
        this.providerClient = providerClient;
    }

    public ImmutableList<ResponsePartMismatch> evaluate(Pact pact) {
        return fromSeq(pact.interactions()).stream()
                .flatMap(this::evaluateInteraction)
                .collect(toImmutableList());
    }

    private Stream<ResponsePartMismatch> evaluateInteraction(Interaction interaction) {
        if (!interaction.providerState().isEmpty()) {
            stateProvider.setState(interaction.providerState().get());
        }

        Response expectedResponse = interaction.response();
        Response actualResponse = providerClient.makeRequest(interaction.request());

        Seq<ResponsePartMismatch> responsePartMismatchSeq = responseMatching.responseMismatches(expectedResponse, actualResponse);
        return fromSeq(responsePartMismatchSeq).stream();
    }



}
