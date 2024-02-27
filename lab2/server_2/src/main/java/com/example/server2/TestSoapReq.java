//package com.example.server2;
//
//import com.example.server2.adapter.LocalDateAdapter;
//import com.example.server2.adapter.LocalDateTimeAdapter;
//import com.example.server2.model.Coordinate;
//import com.example.server2.model.Organization;
//import com.example.server2.model.Position;
//import jakarta.xml.bind.JAXBContext;
//import jakarta.xml.bind.Unmarshaller;
//import jakarta.xml.bind.annotation.XmlAccessType;
//import jakarta.xml.bind.annotation.XmlAccessorType;
//import jakarta.xml.bind.annotation.XmlElement;
//import jakarta.xml.bind.annotation.XmlRootElement;
//import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.security.KeyStore;
//import java.security.KeyStoreException;
//import java.security.cert.CertificateException;
//import java.security.cert.X509Certificate;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
//import javax.net.ssl.*;
//
//public class TestSoapReq {
//    public static void main(String[] args) throws Exception {
//        String keystorePath = "/keystore.jks";
//        String keystorePassword = "123456";
//
//        KeyStore keyStore = KeyStore.getInstance("JKS");
//        try (InputStream is = TestSoapReq.class.getResourceAsStream(keystorePath)) {
//            keyStore.load(is, keystorePassword.toCharArray());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        TrustManager[] trustManagers = new TrustManager[]{new CustomTrustManager(keyStore)};
//        SSLContext sslContext = SSLContext.getInstance("TLS");
//        sslContext.init(null, trustManagers, null);
//
////        String serviceURL = "https://localhost:9001/testsoap";
//        String serviceURL = "https://localhost:9001/testsoap2";
//        URL url = new URL(serviceURL);
//        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
//        connection.setSSLSocketFactory(sslContext.getSocketFactory());
//        connection.setRequestMethod("POST");
//        connection.setRequestProperty("Content-Type", "application/soap+xml");
//        connection.setDoOutput(true);
//
////        String soapRequest = "<soapenv:Envelope " +
////                "xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
////                "xmlns:m=\"https://localhost:9000/company/worker\">" +
////                "<soapenv:Header/>" +
////                "<soapenv:Body>" +
////                "<m:GetWorker>" +
////                "<m:Method>GET</m:Method>" +
////                "<m:URL>https://localhost:9000/company/workers/22</m:URL>" +
////                "</m:GetWorker>" +
////                "</soapenv:Body>" +
////                "</soapenv:Envelope>";
//        String soapRequest = "<soapenv:Envelope " +
//                "xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
//                "xmlns:m=\"https://localhost:9000/company/worker\">" +
//                "<soapenv:Header/>" +
//                "<soapenv:Body>" +
//                "<m:GetWorker>" +
//                "<m:Method>GET</m:Method>" +
//                "<m:URL>https://localhost:9000/company/organization/1</m:URL>" +
//                "</m:GetWorker>" +
//                "</soapenv:Body>" +
//                "</soapenv:Envelope>";
//
//        try (OutputStream os = connection.getOutputStream()) {
//            byte[] input = soapRequest.getBytes("utf-8");
//            os.write(input, 0, input.length);
//        }
//
//        int responseCode = connection.getResponseCode();
//
//        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
//            String line;
//            StringBuilder response = new StringBuilder();
//            while ((line = in.readLine()) != null) {
//                response.append(line);
//            }
//            System.out.println(response);
//        }
////        } else {
////            System.out.println("HTTP Request Failed with error code: " + responseCode);
////        }
//    }
//    static class CustomTrustManager implements X509TrustManager {
//        private final KeyStore trustStore;
//
//        public CustomTrustManager(KeyStore trustStore) {
//            this.trustStore = trustStore;
//        }
//
//        @Override
//        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//        }
//
//        @Override
//        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//            try {
//                for (X509Certificate certificate : chain) {
//                    if (trustStore.getCertificateAlias(certificate) == null) {
//                        throw new CertificateException("Untrusted certificate: " + certificate.getSubjectDN());
//                    }
//                }
//            } catch (KeyStoreException e) {
//                throw new CertificateException("Error checking server certificate", e);
//            }
//        }
//
//        @Override
//        public X509Certificate[] getAcceptedIssuers() {
//            return new X509Certificate[0];
//        }
//    }
//}
