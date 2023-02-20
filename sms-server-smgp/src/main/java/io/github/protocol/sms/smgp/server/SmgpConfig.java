package io.github.protocol.sms.smgp.server;

public class SmgpConfig {

    public String host = "0.0.0.0";

    public int port;

    public int acceptorThreadsNum;

    public int ioThreadsNum;

    public SmgpConfig host(String host) {
        this.host = host;
        return this;
    }

    public SmgpConfig port(int port) {
        this.port = port;
        return this;
    }
}
