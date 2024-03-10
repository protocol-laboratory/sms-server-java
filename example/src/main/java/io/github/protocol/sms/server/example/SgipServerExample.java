package io.github.protocol.sms.server.example;

import io.github.protocol.sms.sgip.server.SgipConfig;
import io.github.protocol.sms.sgip.server.SgipServer;

public class SgipServerExample {
    public static void main(String[] args) throws Exception {
        SgipConfig config = new SgipConfig();
        config = config.port(PortConst.SGIP_DEFAULT_PORT);
        SgipServer sgipServer = new SgipServer(config);
        sgipServer.start();
    }
}
