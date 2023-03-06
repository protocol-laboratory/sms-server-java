package io.github.protocol.sms.smpp.server;

import io.github.protocol.codec.smpp.SmppBindReceiver;
import io.github.protocol.codec.smpp.SmppBindTransceiver;
import io.github.protocol.codec.smpp.SmppBindTransmitter;
import io.github.protocol.codec.smpp.SmppDeliverSm;
import io.github.protocol.codec.smpp.SmppEnquireLink;
import io.github.protocol.codec.smpp.SmppQuerySm;
import io.github.protocol.codec.smpp.SmppSubmitMulti;
import io.github.protocol.codec.smpp.SmppSubmitSm;
import io.github.protocol.codec.smpp.SmppUnbind;

public interface SmppListener {

    void onBindReceiver(SmppBindReceiver msg);

    void onBindTransmitter(SmppBindTransmitter msg);

    void onQuerySm(SmppQuerySm msg);

    void onSubmitSm(SmppSubmitSm msg);

    void onDeliverSm(SmppDeliverSm msg);

    void onUnbind(SmppUnbind msg);

    void onBindTransceiver(SmppBindTransceiver msg);

    void onEnquireLink(SmppEnquireLink msg);

    void onSubmitMulti(SmppSubmitMulti msg);
}
