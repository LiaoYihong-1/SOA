package com.example.server2;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
public class MyTrustManager implements X509TrustManager {
    private final KeyStore trustStore;

    public MyTrustManager(KeyStore trustStore) {
        this.trustStore = trustStore;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        try {
            for (X509Certificate certificate : chain) {
                if (trustStore.getCertificateAlias(certificate) == null) {
                    throw new CertificateException("Untrusted certificate: " + certificate.getSubjectDN());
                }
            }
        } catch (KeyStoreException e) {
            throw new CertificateException("Error checking server certificate", e);
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
