package io.github.protocol.sms.server.util;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;

public class SslContextUtil {

    public static SslContext buildFromJks(String keyPath, String keyPassword, String trustPath, String trustPassword) {
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(Files.newInputStream(Paths.get(keyPath)), keyPassword.toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, keyPassword.toCharArray());
            KeyStore trustStore = KeyStore.getInstance("JKS");
            trustStore.load(Files.newInputStream(Paths.get(trustPath)), trustPassword.toCharArray());
            String defaultTrustAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(defaultTrustAlgorithm);
            trustManagerFactory.init(trustStore);
            return SslContextBuilder.forServer(keyManagerFactory).trustManager(trustManagerFactory).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
