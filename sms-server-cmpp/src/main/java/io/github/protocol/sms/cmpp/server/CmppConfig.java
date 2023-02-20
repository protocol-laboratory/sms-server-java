package io.github.protocol.sms.cmpp.server;

public class CmppConfig {

    public String host = "0.0.0.0";

    public int port;

    public int acceptorThreadsNum;

    public int ioThreadsNum;

    public CmppConfig host(String host) {
        this.host = host;
        return this;
    }

    public CmppConfig port(int port) {
        this.port = port;
        return this;
    }
}
