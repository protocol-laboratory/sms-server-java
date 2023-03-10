package io.github.protocol.sms.smgp.server;

import lombok.ToString;

import java.util.Set;

@ToString
public class SmgpConfig {

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

    public SmgpConfig host(String host) {
        this.host = host;
        return this;
    }

    public SmgpConfig port(int port) {
        this.port = port;
        return this;
    }

    public SmgpConfig acceptorThreadsNum(int acceptorThreadsNum) {
        this.acceptorThreadsNum = acceptorThreadsNum;
        return this;
    }

    public SmgpConfig ioThreadsNum(int ioThreadsNum) {
        this.ioThreadsNum = ioThreadsNum;
        return this;
    }

    public SmgpConfig useSsl(boolean useSsl) {
        this.useSsl = useSsl;
        return this;
    }

    public SmgpConfig keyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
        return this;
    }

    public SmgpConfig keyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
        return this;
    }

    public SmgpConfig trustStorePath(String trustStorePath) {
        this.trustStorePath = trustStorePath;
        return this;
    }

    public SmgpConfig trustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
        return this;
    }

    public SmgpConfig skipSslVerify(boolean skipSslVerify) {
        this.skipSslVerify = skipSslVerify;
        return this;
    }

    public SmgpConfig ciphers(Set<String> ciphers) {
        this.ciphers = ciphers;
        return this;
    }
}
