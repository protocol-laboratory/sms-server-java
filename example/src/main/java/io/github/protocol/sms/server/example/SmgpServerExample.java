package io.github.protocol.sms.server.example;

import io.github.protocol.sms.smgp.server.SmgpConfig;
import io.github.protocol.sms.smgp.server.SmgpServer;

public class SmgpServerExample {
    public static void main(String[] args) throws Exception {
        SmgpConfig config = new SmgpConfig();
        config = config.port(PortConst.SMGP_DEFAULT_PORT);
        SmgpServer smgpServer = new SmgpServer(config);
        smgpServer.start();
    }
}
