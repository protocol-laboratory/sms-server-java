package io.github.protocol.sms.cngp.server;

import java.util.Set;

public class CngpConfig {

    public String host = "0.0.0.0";

    public int port;

    public int acceptorThreadsNum;

    public int ioThreadsNum;

    public boolean useSsl;

    public String keyStorePath;

    public String keyStorePassword;

    public String trustStorePath;

    public String trustStorePassword;

    public boolean skipSslVerify;

    public Set<String> ciphers;

    public CngpConfig host(String host) {
        this.host = host;
        return this;
    }

    public CngpConfig port(int port) {
        this.port = port;
        return this;
    }

    public CngpConfig acceptorThreadsNum(int acceptorThreadsNum) {
        this.acceptorThreadsNum = acceptorThreadsNum;
        return this;
    }

    public CngpConfig ioThreadsNum(int ioThreadsNum) {
        this.ioThreadsNum = ioThreadsNum;
        return this;
    }

    public CngpConfig useSsl(boolean useSsl) {
        this.useSsl = useSsl;
        return this;
    }

    public CngpConfig keyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
        return this;
    }

    public CngpConfig keyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
        return this;
    }

    public CngpConfig trustStorePath(String trustStorePath) {
        this.trustStorePath = trustStorePath;
        return this;
    }

    public CngpConfig trustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
        return this;
    }

    public CngpConfig skipSslVerify(boolean skipSslVerify) {
        this.skipSslVerify = skipSslVerify;
        return this;
    }

    public CngpConfig ciphers(Set<String> ciphers) {
        this.ciphers = ciphers;
        return this;
    }
}
