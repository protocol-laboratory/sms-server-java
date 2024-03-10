package io.github.protocol.sms.server.example;

import io.github.protocol.sms.cmpp.server.CmppConfig;
import io.github.protocol.sms.cmpp.server.CmppServer;

public class CmppServerExample {
    public static void main(String[] args) throws Exception {
        CmppConfig config = new CmppConfig();
        config = config.port(PortConst.CMPP_DEFAULT_PORT);
        CmppServer cmppServer = new CmppServer(config);
        cmppServer.start();
    }
}
