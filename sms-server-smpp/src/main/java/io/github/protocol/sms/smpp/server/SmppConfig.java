package io.github.protocol.sms.smpp.server;

import lombok.ToString;

import java.util.Set;

@ToString
public class SmppConfig {

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

    public SmppConfig skipSslVerify(boolean skipSslVerify) {
        this.skipSslVerify = skipSslVerify;
        return this;
    }

    public SmppConfig ciphers(Set<String> ciphers) {
        this.ciphers = ciphers;
        return this;
    }
}
