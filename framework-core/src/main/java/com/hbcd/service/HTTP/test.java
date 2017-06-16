package com.hbcd.service.HTTP;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;

public class test {

    public static void testStatusCode(String restURL) throws Exception {

        HttpRequest request = new HttpGet(restURL);
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute((HttpUriRequest) request);

        //Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(),HttpStatus.SC_OK);
    }

    public static void testMimeType(String restURL, String expectedMimeType) throws Exception {

        HttpUriRequest request = new HttpGet(restURL);
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        //Assert.assertEquals(expectedMimeType,ContentType.getOrDefault(httpResponse.getEntity()).getMimeType());
    }

    //Test XML
    public static void testContent(String restURL, String element, String expectedValue) throws Exception {

        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(restURL);
        NodeList nodelist = doc.getElementsByTagName(element);

        //Assert.assertEquals(expectedValue,nodelist.item(0).getTextContent());
    }


    public static void testContentJSON(String restURL, String element, String expectedValue) throws Exception {

        HttpUriRequest request = new HttpGet(restURL);
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Convert the response to a String format
        String result = EntityUtils.toString(httpResponse.getEntity());

        // Convert the result as a String to a JSON object
        JSONObject jo = new JSONObject(result);

        //Assert.assertEquals(expectedValue, jo.getString(element));
    }

}
