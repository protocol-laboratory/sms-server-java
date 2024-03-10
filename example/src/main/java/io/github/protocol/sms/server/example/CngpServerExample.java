package io.github.protocol.sms.server.example;

import io.github.protocol.sms.cngp.server.CngpConfig;
import io.github.protocol.sms.cngp.server.CngpServer;

public class CngpServerExample {
    public static void main(String[] args) throws Exception {
        CngpConfig config = new CngpConfig();
        config = config.port(PortConst.CNGP_DEFAULT_PORT);
        CngpServer cngpServer = new CngpServer(config);
        cngpServer.start();
    }
}
