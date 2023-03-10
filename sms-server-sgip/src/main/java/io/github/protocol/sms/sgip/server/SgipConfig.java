package io.github.protocol.sms.sgip.server;

import lombok.ToString;

import java.util.Set;

@ToString
public class SgipConfig {

    public String host = "0.0.0.0";

    public int port;

    public int acceptorThreadsNum;

    public int ioThreadsNum;

    public boolean useSsl;

    public String keyStorePath;

    @ToString.Exclude
    public String keyStorePassword;

    public String trustStorePath;

    @ToString.Exclude
    public String trustStorePassword;

    public boolean skipSslVerify;

    public Set<String> ciphers;

    public SgipConfig host(String host) {
        this.host = host;
        return this;
    }

    public SgipConfig port(int port) {
        this.port = port;
        return this;
    }

    public SgipConfig acceptorThreadsNum(int acceptorThreadsNum) {
        this.acceptorThreadsNum = acceptorThreadsNum;
        return this;
    }

    public SgipConfig ioThreadsNum(int ioThreadsNum) {
        this.ioThreadsNum = ioThreadsNum;
        return this;
    }

    public SgipConfig useSsl(boolean useSsl) {
        this.useSsl = useSsl;
        return this;
    }

    public SgipConfig keyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
        return this;
    }

    public SgipConfig keyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
        return this;
    }

    public SgipConfig trustStorePath(String trustStorePath) {
        this.trustStorePath = trustStorePath;
        return this;
    }

    public SgipConfig trustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
        return this;
    }

    public SgipConfig skipSslVerify(boolean skipSslVerify) {
        this.skipSslVerify = skipSslVerify;
        return this;
    }

    public SgipConfig ciphers(Set<String> ciphers) {
        this.ciphers = ciphers;
        return this;
    }
}
