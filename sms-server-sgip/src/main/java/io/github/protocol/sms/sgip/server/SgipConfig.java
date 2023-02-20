package io.github.protocol.sms.sgip.server;

public class SgipConfig {

    public String host = "0.0.0.0";

    public int port;

    public int acceptorThreadsNum;

    public int ioThreadsNum;

    public SgipConfig host(String host) {
        this.host = host;
        return this;
    }

    public SgipConfig port(int port) {
        this.port = port;
        return this;
    }
}
