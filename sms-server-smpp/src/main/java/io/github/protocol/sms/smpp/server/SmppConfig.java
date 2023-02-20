package io.github.protocol.sms.smpp.server;

public class SmppConfig {

    public String host = "0.0.0.0";

    public int port;

    public int acceptorThreadsNum;

    public int ioThreadsNum;

    public SmppConfig host(String host) {
        this.host = host;
        return this;
    }

    public SmppConfig port(int port) {
        this.port = port;
        return this;
    }
}
