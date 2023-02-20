package io.github.protocol.sms.cngp.server;

public class CngpConfig {

    public String host = "0.0.0.0";

    public int port;

    public int acceptorThreadsNum;

    public int ioThreadsNum;

    public CngpConfig host(String host) {
        this.host = host;
        return this;
    }

    public CngpConfig port(int port) {
        this.port = port;
        return this;
    }
}
