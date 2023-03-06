package io.github.protocol.sms.server.util;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;

public class SslContextUtil {

    public static SslContext buildFromJks(String keyPath, String keyPassword, String trustPath, String trustPassword) {
        try(InputStream keyStream = Files.newInputStream(Paths.get(keyPath));
            InputStream trustStream = Files.newInputStream(Paths.get(trustPath))) {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(keyStream, keyPassword.toCharArray());
            String defaultKeyAlgorithm = KeyManagerFactory.getDefaultAlgorithm();
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(defaultKeyAlgorithm);
            keyManagerFactory.init(keyStore, keyPassword.toCharArray());
            KeyStore trustStore = KeyStore.getInstance("JKS");
            trustStore.load(trustStream, trustPassword.toCharArray());
            String defaultTrustAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(defaultTrustAlgorithm);
            trustManagerFactory.init(trustStore);
            return SslContextBuilder.forServer(keyManagerFactory).trustManager(trustManagerFactory).build();
        } catch (IOException ioe) {
            throw new RuntimeException("Failed to load key store", ioe);
        } catch (Exception e) {
            throw new RuntimeException("Failed to build ssl context", e);
        }
    }
}
