package io.github.protocol.sms.smpp.server;

import io.github.protocol.codec.smpp.SmppDeliverSmBody;
import io.github.protocol.codec.smpp.SmppSubmitSm;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class SmppSubmitSmDummyProcessor implements SmppSubmitSmProcessor {
    @Override
    public Optional<CompletableFuture<SmppDeliverSmBody>> process(SmppSubmitSm submitSm, String messageId) {
        return Optional.empty();
    }
}
