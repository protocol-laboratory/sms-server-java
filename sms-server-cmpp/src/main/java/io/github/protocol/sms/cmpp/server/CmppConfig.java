package io.github.protocol.sms.cmpp.server;

public class CmppConfig {

    public String host = "0.0.0.0";

    public int port;

    public int acceptorThreadsNum;

    public int ioThreadsNum;

    public boolean useSsl;

    public String keyStorePath;

    public String keyStorePassword;

    public String trustStorePath;

    public String trustStorePassword;

    public CmppConfig host(String host) {
        this.host = host;
        return this;
    }

    public CmppConfig port(int port) {
        this.port = port;
        return this;
    }

    public CmppConfig acceptorThreadsNum(int acceptorThreadsNum) {
        this.acceptorThreadsNum = acceptorThreadsNum;
        return this;
    }

    public CmppConfig ioThreadsNum(int ioThreadsNum) {
        this.ioThreadsNum = ioThreadsNum;
        return this;
    }

    public CmppConfig useSsl(boolean useSsl) {
        this.useSsl = useSsl;
        return this;
    }

    public CmppConfig keyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
        return this;
    }

    public CmppConfig keyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
        return this;
    }

    public CmppConfig trustStorePath(String trustStorePath) {
        this.trustStorePath = trustStorePath;
        return this;
    }

    public CmppConfig trustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
        return this;
    }
}
