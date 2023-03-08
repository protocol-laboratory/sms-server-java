package io.github.protocol.sms.server.util;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Set;

public class SslContextUtil {

    public static SslContext buildFromJks(String keyPath,
                                          String keyPassword,
                                          String trustPath,
                                          String trustPassword,
                                          boolean skipSslVerify,
                                          Set<String> ciphers) {
        SslContextBuilder sslContextBuilder;
        try (InputStream keyStream = Files.newInputStream(Paths.get(keyPath))) {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(keyStream, keyPassword.toCharArray());
            String defaultKeyAlgorithm = KeyManagerFactory.getDefaultAlgorithm();
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(defaultKeyAlgorithm);
            keyManagerFactory.init(keyStore, keyPassword.toCharArray());
            sslContextBuilder = SslContextBuilder.forServer(keyManagerFactory);
        } catch (IOException ioe) {
            throw new RuntimeException("Failed to load key store", ioe);
        } catch (UnrecoverableKeyException | CertificateException | KeyStoreException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        try (InputStream trustStream = Files.newInputStream(Paths.get(trustPath))) {
            if (skipSslVerify) {
                sslContextBuilder.trustManager(InsecureTrustManagerFactory.INSTANCE);
            } else {
                KeyStore trustStore = KeyStore.getInstance("JKS");
                trustStore.load(trustStream, trustPassword.toCharArray());
                String defaultTrustAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(defaultTrustAlgorithm);
                trustManagerFactory.init(trustStore);
                sslContextBuilder.trustManager(trustManagerFactory);
            }
            if (ciphers != null) {
                sslContextBuilder.ciphers(ciphers);
            }
        } catch (IOException ioe) {
            throw new RuntimeException("Failed to load trust store", ioe);
        } catch (CertificateException | KeyStoreException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        try {
            return sslContextBuilder.build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to build ssl context", e);
        }
    }
}
