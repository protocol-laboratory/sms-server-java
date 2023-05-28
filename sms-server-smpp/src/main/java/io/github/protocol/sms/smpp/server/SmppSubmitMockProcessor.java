package io.github.protocol.sms.smpp.server;

import io.github.protocol.codec.smpp.EsmClassConst;
import io.github.protocol.codec.smpp.SmppDeliverSmBody;
import io.github.protocol.codec.smpp.SmppSubmitSm;
import io.netty.util.HashedWheelTimer;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SmppSubmitMockProcessor implements SmppSubmitSmProcessor {

    private final HashedWheelTimer timer = new HashedWheelTimer();

    private final long mockDelayInMill;

    private final String mockReportStatus;

    public SmppSubmitMockProcessor() {
        this(0, "DELIVRD");
    }

    public SmppSubmitMockProcessor(long mockDelayInMill, String mockReportStatus) {
        this.mockDelayInMill = mockDelayInMill;
        this.mockReportStatus = mockReportStatus;
    }

    @Override
    public Optional<CompletableFuture<SmppDeliverSmBody>> process(SmppSubmitSm submitSm, String messageId) {
        CompletableFuture<SmppDeliverSmBody> future = new CompletableFuture<>();
        timer.newTimeout(timeout -> {
            SmppDeliverSmBody deliverSmBody = createSmppDeliverSmBody(submitSm, messageId, mockReportStatus);
            future.complete(deliverSmBody);
        }, mockDelayInMill, TimeUnit.MILLISECONDS);
        return Optional.of(future);
    }

    private SmppDeliverSmBody createSmppDeliverSmBody(SmppSubmitSm submitSm, String southMsgId, String status) {
        StringBuilder report = new StringBuilder();
        report.append("id:").append(southMsgId).append(" ")
                .append("stat:").append(status).append(" ")
                .append("err:").append("0").append(" ");

        byte dataCoding = 8;
        byte[] content;
        try {
            content = report.toString().getBytes("UnicodeBigUnmarked");
        } catch (UnsupportedEncodingException e) {
            log.error("unsupported UnicodeBigUnmarked, fall back to ascii", e);
            dataCoding = 0;
            content = report.toString().getBytes(StandardCharsets.US_ASCII);
        }

        byte defaultValue = 1;
        return new SmppDeliverSmBody("1",
                defaultValue,
                defaultValue,
                submitSm.body().destinationAddr(),
                defaultValue,
                defaultValue,
                submitSm.body().sourceAddr(),
                EsmClassConst.FORWARD,
                defaultValue,
                defaultValue,
                null,
                "1",
                defaultValue,
                defaultValue,
                dataCoding,
                defaultValue,
                (short) content.length,
                content);
    }

    public void close() {
        timer.stop();
    }
}
