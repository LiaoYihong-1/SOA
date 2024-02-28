package com.example.server2;

import com.example.server2.model.*;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.ws.rs.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPFactory;
import jakarta.xml.soap.SOAPFault;
import jakarta.xml.ws.soap.SOAPFaultException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.net.ssl.*;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@XmlRootElement(name = "workerRequest", namespace = "https://localhost:9000/company/worker")
@Data
class SoapRequest {

    private WorkerInfo workerInfo;

    @XmlElement(name = "WorkerInfo")
    public WorkerInfo getWorkerInfo() {
        return workerInfo;
    }

}

@WebService
@Slf4j
@RequiredArgsConstructor
public class HrResource implements HrRes {


    @Override
    public Worker test() throws SOAPException {

        SOAPFactory soapFactory = SOAPFactory.newInstance();
        SOAPFault soapFault = soapFactory.createFault(
                "400,Internal Server Error",
                new QName("http://schemas.xmlsoap.org/soap/envelope/", "Client"));
        throw new SOAPFaultException(soapFault);

    }

    @Override
    public String fire(@WebParam(name = "id") String id_new)
            throws SOAPException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException, IOException {

        Integer id = null;

        try {
            id = Integer.parseInt(id_new);
        } catch (NumberFormatException e) {
            SOAPFactory soapFactory = SOAPFactory.newInstance();
            SOAPFault soapFault = soapFactory.createFault(
                    "400,Invalid request",
                    new QName("http://schemas.xmlsoap.org/soap/envelope/", "Client"));
            throw new SOAPFaultException(soapFault);
        }
        String keystorePath = "/keystore.jks";
        String keystorePassword = "123456";

        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (InputStream is = HrResource.class.getResourceAsStream(keystorePath)) {
            keyStore.load(is, keystorePassword.toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
        }

        TrustManager[] trustManagers = new TrustManager[]{new MyTrustManager(keyStore)};
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagers, null);

        String serviceURL1 = "https://localhost:9001/testsoap";
        URL url = new URL(serviceURL1);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setSSLSocketFactory(sslContext.getSocketFactory());
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/soap+xml");
        connection.setDoOutput(true);

        String soapRequestWorker = "<soapenv:Envelope " +
                "xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "xmlns:m=\"https://localhost:9000/company/worker\">" +
                "<soapenv:Header/>" +
                "<soapenv:Body>" +
                "<m:GetWorker>" +
                "<m:Method>GET</m:Method>" +
                "<m:URL>https://localhost:9000/company/workers/" + id + "</m:URL>" +
                "</m:GetWorker>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";


        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = soapRequestWorker.getBytes("utf-8");
            os.write(input, 0, input.length);
        } catch (Exception ex) {
            SOAPFactory soapFactory = SOAPFactory.newInstance();
            SOAPFault soapFault = soapFactory.createFault(
                    "400,Invalid request",
                    new QName("http://schemas.xmlsoap.org/soap/envelope/", "Client"));
            throw new SOAPFaultException(soapFault);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            SOAPFactory soapFactory = SOAPFactory.newInstance();
            SOAPFault soapFault = soapFactory.createFault(
                    "400,Invalid request",
                    new QName("http://schemas.xmlsoap.org/soap/envelope/", "Client"));
            throw new SOAPFaultException(soapFault);
        }
        Worker workerFullInfo = null;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            Document doc = factory.newDocumentBuilder().parse(new InputSource(new StringReader(response.toString())));
            NodeList bodyList = doc.getElementsByTagNameNS("http://schemas.xmlsoap.org/soap/envelope/", "Body");
            if (bodyList.getLength() > 0) {
                Element bodyElement = (Element) bodyList.item(0);
                Element workerFullInfoElement = (Element) bodyElement.getElementsByTagNameNS("https://localhost:9000/company/worker", "WorkerFullInfo").item(0);
                JAXBContext jaxbContext = JAXBContext.newInstance(Worker.class);
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                workerFullInfo = (Worker) unmarshaller.unmarshal(workerFullInfoElement);
            }
        } catch (JAXBException | SAXException | ParserConfigurationException e) {
            System.out.println("2");
            SOAPFactory soapFactory = SOAPFactory.newInstance();
            SOAPFault soapFault = soapFactory.createFault(
                    "400,Invalid request",
                    new QName("http://schemas.xmlsoap.org/soap/envelope/", "Client"));
            throw new SOAPFaultException(soapFault);
        } catch (Exception e) {
            System.out.println("5");
            SOAPFactory soapFactory = SOAPFactory.newInstance();
            SOAPFault soapFault = soapFactory.createFault(
                    "500,Internal server error",
                    new QName("http://schemas.xmlsoap.org/soap/envelope/", "Server"));
            throw new SOAPFaultException(soapFault);
        }

        try {

            if (workerFullInfo == null
                    || workerFullInfo.getOrganization() == null
                    || workerFullInfo.getOrganization().getAnnualTurnover() == null
            )
                throw new NotFoundException("Invalid request");
            workerFullInfo.setOrganization(null);

            String serviceURL12 = "https://localhost:9001/putsoap";
            URL url2 = new URL(serviceURL12);
            HttpsURLConnection connection2 = (HttpsURLConnection) url2.openConnection();
            connection2.setSSLSocketFactory(sslContext.getSocketFactory());
            connection2.setRequestMethod("POST");
            connection2.setRequestProperty("Content-Type", "application/soap+xml");
            connection2.setDoOutput(true);


            SoapRequest soapRequest = new SoapRequest();
            soapRequest.setWorkerInfo(WorkerInfo.ConvertWorker(workerFullInfo));

            String soapRequestXml = null;

            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(SoapRequest.class);
                Marshaller marshaller = jaxbContext.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
                StringWriter stringWriter = new StringWriter();
                marshaller.marshal(soapRequest, stringWriter);
                soapRequestXml = stringWriter.toString();
            } catch (JAXBException e) {
                System.out.println("1");
                SOAPFactory soapFactory = SOAPFactory.newInstance();
                SOAPFault soapFault = soapFactory.createFault(
                        "400,Invalid request",
                        new QName("http://schemas.xmlsoap.org/soap/envelope/", "Client"));
                throw new SOAPFaultException(soapFault);
            }

            String putWorker = "<soapenv:Envelope " +
                    "xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                    "xmlns:m=\"https://localhost:9000/company/worker\">" +
                    "<soapenv:Header/>" +
                    "<soapenv:Body>" +
                    "<m:PutWorker>" +
                    "<m:Method>PUT</m:Method>" +
                    "<m:URL>https://localhost:9000/company/workers/" + id_new + "</m:URL>" +
                    "<m:Payload>" + soapRequestXml + "</m:Payload>" +
                    "</m:PutWorker>" +
                    "</soapenv:Body>" +
                    "</soapenv:Envelope>";

            try (OutputStream os = connection2.getOutputStream()) {
                byte[] input = putWorker.getBytes("utf-8");
                os.write(input, 0, input.length);
            } catch (Exception ex) {
                SOAPFactory soapFactory = SOAPFactory.newInstance();
                SOAPFault soapFault = soapFactory.createFault(
                        "500,Internal server error",
                        new QName("http://schemas.xmlsoap.org/soap/envelope/", "Server"));
                throw new SOAPFaultException(soapFault);
            }

            int responseCodePut = connection2.getResponseCode();
            System.out.println(responseCodePut);
            String resp = "<soapenv:Envelope " +
                    "xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                    "xmlns:m=\"https://localhost:9000/company/worker\">" +
                    "<soapenv:Header/>" +
                    "<soapenv:Body>" +
                    "<m:MoveWorkerResponse>" +
                    "<m:RespCode>204</m:RespCode>" +
                    "</m:MoveWorkerResponse>" +
                    "</soapenv:Body>" +
                    "</soapenv:Envelope>";
            System.out.println("wtf");
            return resp;
        } catch (NotFoundException notFoundException) {
            System.out.println("3");
            SOAPFactory soapFactory = SOAPFactory.newInstance();
            SOAPFault soapFault = soapFactory.createFault(
                    "400,Invalid request",
                    new QName("http://schemas.xmlsoap.org/soap/envelope/", "Client"));
            throw new SOAPFaultException(soapFault);
        } catch (Exception e) {
            System.out.println("4");
            SOAPFactory soapFactory = SOAPFactory.newInstance();
            SOAPFault soapFault = soapFactory.createFault(
                    "500,Internal server error",
                    new QName("http://schemas.xmlsoap.org/soap/envelope/", "Server"));
            throw new SOAPFaultException(soapFault);
        }
    }

    @Override
    public String move(@WebParam(name = "id") String workerIdStr,
                       @WebParam(name = "id-from") String orgFromIdStr,
                       @WebParam(name = "id-to") String orgToIdStr) throws SOAPException {

        Integer workerId = null;
        Integer orgFromId = null;
        Integer orgToId = null;

        try {
            workerId = Integer.parseInt(workerIdStr);
            orgFromId = Integer.parseInt(orgFromIdStr);
            orgToId = Integer.parseInt(orgToIdStr);
        } catch (NumberFormatException e) {
            SOAPFactory soapFactory = SOAPFactory.newInstance();
            SOAPFault soapFault = soapFactory.createFault(
                    "400,Invalid request",
                    new QName("http://schemas.xmlsoap.org/soap/envelope/", "Client"));
            throw new SOAPFaultException(soapFault);
        }
        try {
            String keystorePath = "/keystore.jks";
            String keystorePassword = "123456";

            KeyStore keyStore = KeyStore.getInstance("JKS");
            try (InputStream is = HrResource.class.getResourceAsStream(keystorePath)) {
                keyStore.load(is, keystorePassword.toCharArray());
            } catch (Exception e) {
                e.printStackTrace();
            }

            TrustManager[] trustManagers = new TrustManager[]{new MyTrustManager(keyStore)};
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers, null);

            String serviceURL1 = "https://localhost:9001/testsoap";
            URL url = new URL(serviceURL1);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(sslContext.getSocketFactory());
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/soap+xml");
            connection.setDoOutput(true);

            String soapRequestWorker = "<soapenv:Envelope " +
                    "xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                    "xmlns:m=\"https://localhost:9000/company/worker\">" +
                    "<soapenv:Header/>" +
                    "<soapenv:Body>" +
                    "<m:GetWorker>" +
                    "<m:Method>GET</m:Method>" +
                    "<m:URL>https://localhost:9000/company/workers/" + workerId + "</m:URL>" +
                    "</m:GetWorker>" +
                    "</soapenv:Body>" +
                    "</soapenv:Envelope>";

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = soapRequestWorker.getBytes("utf-8");
                os.write(input, 0, input.length);
            } catch (Exception ex) {
                SOAPFactory soapFactory = SOAPFactory.newInstance();
                SOAPFault soapFault = soapFactory.createFault(
                        "500,Internal server error",
                        new QName("http://schemas.xmlsoap.org/soap/envelope/", "Server"));
                throw new SOAPFaultException(soapFault);
            }

            int responseCode = connection.getResponseCode();
            System.out.println(responseCode);
            if (responseCode != 200) {
                throw new NotFoundException();
            }

            Worker workerFullInfo = null;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                System.out.println(response);
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setNamespaceAware(true);
                Document doc = factory.newDocumentBuilder().parse(new InputSource(new StringReader(response.toString())));
                NodeList bodyList = doc.getElementsByTagNameNS("http://schemas.xmlsoap.org/soap/envelope/", "Body");
                if (bodyList.getLength() > 0) {
                    Element bodyElement = (Element) bodyList.item(0);
                    Element workerFullInfoElement = (Element) bodyElement.getElementsByTagNameNS("https://localhost:9000/company/worker", "WorkerFullInfo").item(0);
                    JAXBContext jaxbContext = JAXBContext.newInstance(Worker.class);
                    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                    workerFullInfo = (Worker) unmarshaller.unmarshal(workerFullInfoElement);
                }
            } catch (JAXBException | SAXException | ParserConfigurationException e) {
                System.out.println("2");
                SOAPFactory soapFactory = SOAPFactory.newInstance();
                SOAPFault soapFault = soapFactory.createFault(
                        "400,Invalid request",
                        new QName("http://schemas.xmlsoap.org/soap/envelope/", "Client"));
                throw new SOAPFaultException(soapFault);
            } catch (Exception e) {
                System.out.println("5");
                SOAPFactory soapFactory = SOAPFactory.newInstance();
                SOAPFault soapFault = soapFactory.createFault(
                        "500,Internal server error",
                        new QName("http://schemas.xmlsoap.org/soap/envelope/", "Server"));
                throw new SOAPFaultException(soapFault);
            }

            String serviceURL2 = "https://localhost:9001/testsoap2";
            URL url2 = new URL(serviceURL2);
            HttpsURLConnection connection2 = (HttpsURLConnection) url2.openConnection();
            connection2.setSSLSocketFactory(sslContext.getSocketFactory());
            connection2.setRequestMethod("POST");
            connection2.setRequestProperty("Content-Type", "application/soap+xml");
            connection2.setDoOutput(true);
            String soapRequestOrganization = "<soapenv:Envelope " +
                    "xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                    "xmlns:m=\"https://localhost:9000/company/worker\">" +
                    "<soapenv:Header/>" +
                    "<soapenv:Body>" +
                    "<m:GetWorker>" +
                    "<m:Method>GET</m:Method>" +
                    "<m:URL>https://localhost:9000/company/organization/" + orgFromId + "</m:URL>" +
                    "</m:GetWorker>" +
                    "</soapenv:Body>" +
                    "</soapenv:Envelope>";

            try (OutputStream os = connection2.getOutputStream()) {
                byte[] input = soapRequestOrganization.getBytes("utf-8");
                os.write(input, 0, input.length);
            } catch (Exception ex) {
                SOAPFactory soapFactory = SOAPFactory.newInstance();
                SOAPFault soapFault = soapFactory.createFault(
                        "500,Internal server error",
                        new QName("http://schemas.xmlsoap.org/soap/envelope/", "Server"));
                throw new SOAPFaultException(soapFault);
            }

            int responseCode2 = connection2.getResponseCode();
            if (responseCode2 != 200) {
                throw new NotFoundException();
            }
            Organization organization = null;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection2.getInputStream()))) {
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                System.out.println(response);
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setNamespaceAware(true);
                Document doc = factory.newDocumentBuilder().parse(new InputSource(new StringReader(response.toString())));
                NodeList bodyList = doc.getElementsByTagNameNS("http://schemas.xmlsoap.org/soap/envelope/", "Body");
                if (bodyList.getLength() > 0) {
                    Element bodyElement = (Element) bodyList.item(0);
                    Element org = (Element) bodyElement.getElementsByTagNameNS("https://localhost:9000/company/worker", "Organization").item(0);
                    JAXBContext jaxbContext = JAXBContext.newInstance(Organization.class);
                    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                    organization = (Organization) unmarshaller.unmarshal(org);
                }
            } catch (JAXBException | SAXException | ParserConfigurationException e) {
                throw new NotFoundException();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("5");
                SOAPFactory soapFactory = SOAPFactory.newInstance();
                SOAPFault soapFault = soapFactory.createFault(
                        "500,Internal server error",
                        new QName("http://schemas.xmlsoap.org/soap/envelope/", "Server"));
                throw new SOAPFaultException(soapFault);
            }
            String serviceURL23 = "https://localhost:9001/testsoap2";
            URL url23 = new URL(serviceURL23);
            HttpsURLConnection connection23 = (HttpsURLConnection) url23.openConnection();
            connection23.setSSLSocketFactory(sslContext.getSocketFactory());
            connection23.setRequestMethod("POST");
            connection23.setRequestProperty("Content-Type", "application/soap+xml");
            connection23.setDoOutput(true);


            String soapRequestOrganization3 = "<soapenv:Envelope " +
                    "xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                    "xmlns:m=\"https://localhost:9000/company/worker\">" +
                    "<soapenv:Header/>" +
                    "<soapenv:Body>" +
                    "<m:GetWorker>" +
                    "<m:Method>GET</m:Method>" +
                    "<m:URL>https://localhost:9000/company/organization/" + orgToId + "</m:URL>" +
                    "</m:GetWorker>" +
                    "</soapenv:Body>" +
                    "</soapenv:Envelope>";

            try (OutputStream os = connection23.getOutputStream()) {
                byte[] input = soapRequestOrganization3.getBytes("utf-8");
                os.write(input, 0, input.length);
            } catch (Exception ex) {
                SOAPFactory soapFactory = SOAPFactory.newInstance();
                SOAPFault soapFault = soapFactory.createFault(
                        "500,Internal server error",
                        new QName("http://schemas.xmlsoap.org/soap/envelope/", "Server"));
                throw new SOAPFaultException(soapFault);
            }
            int responseCode23 = connection2.getResponseCode();
            if (responseCode23 != 200) {
                throw new NotFoundException();
            }
            Organization organization2 = null;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection23.getInputStream()))) {
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                System.out.println(response);
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setNamespaceAware(true);
                Document doc = factory.newDocumentBuilder().parse(new InputSource(new StringReader(response.toString())));
                NodeList bodyList = doc.getElementsByTagNameNS("http://schemas.xmlsoap.org/soap/envelope/", "Body");
                if (bodyList.getLength() > 0) {
                    Element bodyElement = (Element) bodyList.item(0);
                    Element org = (Element) bodyElement.getElementsByTagNameNS("https://localhost:9000/company/worker", "Organization").item(0);
                    JAXBContext jaxbContext = JAXBContext.newInstance(Organization.class);
                    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                    organization2 = (Organization) unmarshaller.unmarshal(org);
                }
            } catch (JAXBException | SAXException | ParserConfigurationException e) {
                throw new NotFoundException();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("5");
                SOAPFactory soapFactory = SOAPFactory.newInstance();
                SOAPFault soapFault = soapFactory.createFault(
                        "500,Internal server error",
                        new QName("http://schemas.xmlsoap.org/soap/envelope/", "Server"));
                throw new SOAPFaultException(soapFault);
            }

            if (
                    workerFullInfo.getOrganization() == null
                            || !organization.getId().equals(workerFullInfo.getOrganization().getId())
                            || organization2.getId().equals(workerFullInfo.getOrganization().getId())
            ) {
                throw new NotFoundException("Invalid request");
            }
            workerFullInfo.setOrganization(organization2);

            try {

                String serviceURL12 = "https://localhost:9001/putsoap2";
                URL url22 = new URL(serviceURL12);
                HttpsURLConnection connection22 = (HttpsURLConnection) url22.openConnection();
                connection22.setSSLSocketFactory(sslContext.getSocketFactory());
                connection22.setRequestMethod("POST");
                connection22.setRequestProperty("Content-Type", "application/soap+xml");
                connection22.setDoOutput(true);


                SoapRequest soapRequest = new SoapRequest();
                soapRequest.setWorkerInfo(WorkerInfo.ConvertWorker(workerFullInfo));

                String soapRequestXml = null;

                try {
                    JAXBContext jaxbContext = JAXBContext.newInstance(SoapRequest.class);
                    Marshaller marshaller = jaxbContext.createMarshaller();
                    marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
                    StringWriter stringWriter = new StringWriter();
                    marshaller.marshal(soapRequest, stringWriter);
                    soapRequestXml = stringWriter.toString();
                } catch (JAXBException e) {
                    System.out.println("1");
                    SOAPFactory soapFactory = SOAPFactory.newInstance();
                    SOAPFault soapFault = soapFactory.createFault(
                            "400,Invalid request",
                            new QName("http://schemas.xmlsoap.org/soap/envelope/", "Client"));
                    throw new SOAPFaultException(soapFault);
                }

                String putWorker = "<soapenv:Envelope " +
                        "xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                        "xmlns:m=\"https://localhost:9000/company/worker\">" +
                        "<soapenv:Header/>" +
                        "<soapenv:Body>" +
                        "<m:PutWorker>" +
                        "<m:Method>PUT</m:Method>" +
                        "<m:URL>https://localhost:9000/company/workers/" + workerId + "</m:URL>" +
                        "<m:Payload>" + soapRequestXml + "</m:Payload>" +
                        "</m:PutWorker>" +
                        "</soapenv:Body>" +
                        "</soapenv:Envelope>";

                try (OutputStream os = connection22.getOutputStream()) {
                    byte[] input = putWorker.getBytes("utf-8");
                    os.write(input, 0, input.length);
                } catch (Exception ex) {
                    SOAPFactory soapFactory = SOAPFactory.newInstance();
                    SOAPFault soapFault = soapFactory.createFault(
                            "500,Internal server error",
                            new QName("http://schemas.xmlsoap.org/soap/envelope/", "Server"));
                    throw new SOAPFaultException(soapFault);
                }

                int responseCodePut = connection22.getResponseCode();
                if (responseCodePut == 200) {
                    String putWorker1 = "<soapenv:Envelope " +
                            "xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                            "xmlns:m=\"https://localhost:9000/company/worker\">" +
                            "<soapenv:Header/>" +
                            "<soapenv:Body>" +
                            "<m:MoveWorkerResponse>" +
                            "<m:Payload>" + soapRequestXml + "</m:Payload>" +
                            "</m:MoveWorkerResponse>" +
                            "</soapenv:Body>" +
                            "</soapenv:Envelope>";

                    return putWorker1;
                } else
                    throw new NotFoundException();

            } catch (NotFoundException notFoundException) {
                System.out.println("3");
                SOAPFactory soapFactory = SOAPFactory.newInstance();
                SOAPFault soapFault = soapFactory.createFault(
                        "400,Invalid request",
                        new QName("http://schemas.xmlsoap.org/soap/envelope/", "Client"));
                throw new SOAPFaultException(soapFault);
            } catch (Exception e) {
                System.out.println("4");
                SOAPFactory soapFactory = SOAPFactory.newInstance();
                SOAPFault soapFault = soapFactory.createFault(
                        "500,Internal server error",
                        new QName("http://schemas.xmlsoap.org/soap/envelope/", "Server"));
                throw new SOAPFaultException(soapFault);
            }

        } catch (NotFoundException notFoundException) {
            SOAPFactory soapFactory = SOAPFactory.newInstance();
            SOAPFault soapFault = soapFactory.createFault(
                    "400,Invalid request",
                    new QName("http://schemas.xmlsoap.org/soap/envelope/", "Client"));
            throw new SOAPFaultException(soapFault);
        } catch (Exception e) {
            SOAPFactory soapFactory = SOAPFactory.newInstance();
            SOAPFault soapFault = soapFactory.createFault(
                    "500,Internal server error",
                    new QName("http://schemas.xmlsoap.org/soap/envelope/", "Server"));
            throw new SOAPFaultException(soapFault);
        }
    }
}
