package io.github.protocol.sms.server.example;

import io.github.protocol.sms.smpp.server.SmppConfig;
import io.github.protocol.sms.smpp.server.SmppServer;

public class SmppServerExample {
    public static void main(String[] args) throws Exception {
        SmppConfig config = new SmppConfig();
        config = config.port(PortConst.SMPP_DEFAULT_PORT);
        SmppServer smppServer = new SmppServer(config);
        smppServer.start();
    }
}
