package io.github.protocol.sms.smpp.server;

public class SmppConfig {

    public String host = "0.0.0.0";

    public int port;

    public int acceptorThreadsNum;

    public int ioThreadsNum;

    public boolean useSsl;

    public String keyStorePath;

    public String keyStorePassword;

    public String trustStorePath;

    public String trustStorePassword;

    public SmppConfig host(String host) {
        this.host = host;
        return this;
    }

    public SmppConfig port(int port) {
        this.port = port;
        return this;
    }

    public SmppConfig acceptorThreadsNum(int acceptorThreadsNum) {
        this.acceptorThreadsNum = acceptorThreadsNum;
        return this;
    }

    public SmppConfig ioThreadsNum(int ioThreadsNum) {
        this.ioThreadsNum = ioThreadsNum;
        return this;
    }

    public SmppConfig useSsl(boolean useSsl) {
        this.useSsl = useSsl;
        return this;
    }

    public SmppConfig keyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
        return this;
    }

    public SmppConfig keyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
        return this;
    }

    public SmppConfig trustStorePath(String trustStorePath) {
        this.trustStorePath = trustStorePath;
        return this;
    }

    public SmppConfig trustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
        return this;
    }
}
