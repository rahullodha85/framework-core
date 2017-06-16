package com.hbcd.service.SOAP;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.xml.soap.*;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

//import org.apache.http.client.methods.HttpPost;

public class SOAPRequest {

    private static final String ServiceEndPoint_Gold = "http://qa.promo.ams.saksdirect.com:8080/ams";
    private static final String ServiceEndPoint_DevQA = "http://dev.promo.ams.saksdirect.com:8080/ams";

    private static final String SOAPRequestXML =    //"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ams=\"http://localhost/yb/amssoap\">" +
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ams=\"http://localhost/yb/amssoap\"\n" +
                    "                   xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" +
                    "                   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                    "	<soapenv:Header/>\n" +
                    "	<soapenv:Body>\n" +
                    "	   <ams:processTrans>\n" +
                    "		  <!--Optional:-->\n" +
                    "		  <request>\n" +
                    "			 <!--Zero or more repetitions:-->\n" +
                    "			 <item><key>location</key><value>SaksDirect</value></item>\n" +
                    "			 <item><key>termnum</key><value>12340000</value></item>\n" +
                    "			 <item><key>transnum</key><value>9021000212345678</value></item>\n" +
                    "			 <item><key>querytype</key><value>4</value></item>\n" +
                    "			 <item><key>timestamp</key><value>02/19/14 16:23:22</value></item>\n" +
                    "			 <item><key>languagecode</key><value>en-US</value></item>\n" +
                    "			 <item><key>item10001</key>\n" +
                    "			   <value>0|999100|2|500||1|1|001122|1|1|100001|1|0</value></item>\n" +
                    "			 <item><key>item10002</key>\n" +
                    "			   <value>0|999101|2|4999||1|1|001123|1|1|100002|1|0</value></item>\n" +
                    "			 <item><key>item10003</key>\n" +
                    "			   <value>4|COUPON00PROMO30D||||||433444|</value></item>\n" +
                    "		  </request>\n" +
                    "	   </ams:processTrans>\n" +
                    "	</soapenv:Body>\n" +
                    "</soapenv:Envelope>";

    private static final String msgBody =
            "     <ams:processTrans>\n" +
                    "		  <!--Optional:-->\n" +
                    "		  <request>\n" +
                    "			 <!--Zero or more repetitions:-->\n" +
                    "			 <item><key>location</key><value>SaksDirect</value></item>\n" +
                    "			 <item><key>termnum</key><value>12340000</value></item>\n" +
                    "			 <item><key>transnum</key><value>9021000212345678</value></item>\n" +
                    "			 <item><key>querytype</key><value>4</value></item>\n" +
                    "			 <item><key>timestamp</key><value>02/19/14 16:23:22</value></item>\n" +
                    "			 <item><key>languagecode</key><value>en-US</value></item>\n" +
                    "			 <item><key>item10001</key>\n" +
                    "			   <value>0|999100|2|500||1|1|001122|1|1|100001|1|0</value></item>\n" +
                    "			 <item><key>item10002</key>\n" +
                    "			   <value>0|999101|2|4999||1|1|001123|1|1|100002|1|0</value></item>\n" +
                    "			 <item><key>item10003</key>\n" +
                    "			   <value>4|COUPON00PROMO30D||||||433444|</value></item>\n" +
                    "		  </request>\n" +
                    "	   </ams:processTrans>";


    //WORKING
    public static void get() throws Exception {
        SOAPConnectionFactory soapConnectionFactory;
        try {
            soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection connection = soapConnectionFactory.createConnection();

            MessageFactory factory = MessageFactory.newInstance();
            SOAPMessage soapMessage = factory.createMessage();
            SOAPPart soapPart = soapMessage.getSOAPPart();
            SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
            SOAPHeader soapHeader = soapMessage.getSOAPHeader();
            SOAPBody soapBody = soapMessage.getSOAPBody();


            soapMessage.setProperty(SOAPMessage.WRITE_XML_DECLARATION, "true");
            soapMessage.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "UTF-8");

            soapEnvelope.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding/");
            soapEnvelope.setAttribute("xmlns:SOAP-ENC", "http://schemas.xmlsoap.org/soap/encoding/");
            soapEnvelope.setAttribute("xmlns:ams", "http://localhost/yb/amssoap");


            //soapEnvelope.addNamespaceDeclaration("ams", "http://localhost/yb/amssoap");

            soapHeader.detachNode();
            StreamSource prepMsg = new StreamSource(new ByteArrayInputStream(SOAPRequestXML.getBytes()));
            soapPart.setContent(prepMsg);
            soapMessage.saveChanges();


//	        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
//
//	        f.setNamespaceAware(true);
//	        DocumentBuilder builder = f.newDocumentBuilder();
//	        
//	        soapBody.addDocument(builder.parse(new ByteArrayInputStream(msgBody.getBytes())));

//	        QName bodyName = new QName(ServiceEndPoint,
//	            "GetLastTradePrice", "m");
//	        SOAPBodyElement bodyElement = body.addBodyElement(bodyName);
//	
//	        QName name = new QName("symbol");
//	        SOAPElement symbol = bodyElement.addChildElement(name);
//	        symbol.addTextNode("SUNW");

            URL endpoint = new URL(ServiceEndPoint_DevQA);

            ByteArrayOutputStream baos = null;

            baos = new ByteArrayOutputStream();
            soapMessage.writeTo(baos);
            String messageString = baos.toString();
            baos.close();
            System.out.println(messageString);
            //soapMessage.writeTo(System.out);

            SOAPMessage response = connection.call(soapMessage, endpoint);


            //SOAPBody soapResponseBody = response.getSOAPBody();
//	        System.out.println(response.getContentDescription());

            TransformerFactory tff = TransformerFactory.newInstance();
            Transformer tf = tff.newTransformer();

            // Get reply content
            Source sc = response.getSOAPPart().getContent();

            // Set output transformation
//	    	StreamResult result = new StreamResult(System.out);
//	    	tf.transform(sc, result);
//	    	System.out.println();

            StringWriter outWriter = new StringWriter();
            StreamResult result = new StreamResult(outWriter);
            tf.transform(sc, result);
            StringBuffer sb = outWriter.getBuffer();
            String finalstring = sb.toString();

            System.out.println(finalstring);
            connection.close();

//	        Iterator iterator = soapBody.getChildElements(bodyName);
//	        bodyElement = (SOAPBodyElement)iterator.next();
//	        String lastPrice = bodyElement.getValue();

//	        System.out.print("The last price for SUNW is ");
//	        System.out.println(lastPrice);
        } catch (UnsupportedOperationException | SOAPException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    //WORKING
    public static void callWebService() throws Exception {
        //String body ="<?xml version=\"1.0\" encoding=\"UTF-8\"?><SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns1=\"http://example.com/v1.0/Records\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><SOAP-ENV:Body>"+soapenvbody+"</SOAP-ENV:Body></SOAP-ENV:Envelope>";

        HttpPost httppost = new HttpPost(ServiceEndPoint_DevQA);

        // Request parameters and other properties.
        StringEntity stringentity = new StringEntity(SOAPRequestXML, Consts.UTF_8);
        stringentity.setChunked(true);
        httppost.setEntity(stringentity);
        httppost.addHeader("Accept", "text/xml");
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();

        String strresponse = null;
        if (entity != null) {
            strresponse = EntityUtils.toString(entity);
            System.out.println(strresponse);
        }
    }

    public static void post3() throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        //HttpGet request = new HttpGet(ServiceEndPoint);
        HttpPost httppost = new HttpPost(ServiceEndPoint_DevQA);
        StringEntity stringentity = new StringEntity(SOAPRequestXML, Consts.UTF_8);
        stringentity.setChunked(true);
        httppost.setEntity(stringentity);
        HttpResponse response = client.execute(httppost);
    }

    //Important with AUTHENTICATION 
    public static void get23() throws Exception {
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();

        HttpHost targetHost = new HttpHost("localhost", 80, "http");
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(targetHost.getHostName(), targetHost.getPort()),
                new UsernamePasswordCredentials("username", "password"));

        // Create AuthCache instance
        AuthCache authCache = new BasicAuthCache();
        // Generate BASIC scheme object and add it to the local auth cache
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(targetHost, basicAuth);

        // Add AuthCache to the execution context
        HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(credsProvider);
        context.setAuthCache(authCache);

        HttpGet httpget = new HttpGet("/");
        for (int i = 0; i < 3; i++) {
            CloseableHttpResponse response = httpclient.execute(
                    targetHost, httpget, context);
            try {
                HttpEntity entity = response.getEntity();

            } finally {
                response.close();
            }
        }
    }
}
