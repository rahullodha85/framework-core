package com.hbcd.serivce.REST;

import com.hbcd.utility.testscriptdata.KeyValuePair;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JSONRequest {

    static String BaseURL = "http://qa-s5a-pdp-app.saksdirect.com:8280";
    static String BaseHost = "qa-s5a-pdp-app.saksdirect.com";
    static String BaseURL2 = "http://qa-s5a-pdp-app.saksdirect.com:8280";
    static String BaseURL3 = "http://qa-s5a-pdp-app.saksdirect.com:8280/product/0416696915526";

//==================================== G E T ================================================

    //WORKING
    public static String get(String servicUrl, List<KeyValuePair> headerList) {
        String json = "";

        //Ensure Not NULL
        if (headerList == null) headerList = new ArrayList<KeyValuePair>();

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet(servicUrl);
            request.addHeader("content-type", "application/json");

            for (KeyValuePair kv : headerList) {
                request.addHeader(kv.getKey(), kv.getValue());
            }

            HttpResponse result = httpClient.execute(request);
            json = EntityUtils.toString(result.getEntity(), "UTF-8");
            //System.out.println(json);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return json;
    }

//==================================== END GET ================================================

//==================================== P O S T ================================================	


    //WORKING
    public static String post(String url, List<KeyValuePair> headerList, String jsonRequest) {
        String json = "";

        //Ensure Not NULL
        if (headerList == null) headerList = new ArrayList<KeyValuePair>();

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpPost request = new HttpPost(url);
            StringEntity entity = new StringEntity(jsonRequest, "UTF-8");
            request.addHeader("content-type", "application/json");
            for (KeyValuePair kv : headerList) {
                request.addHeader(kv.getKey(), kv.getValue());
            }
            request.setEntity(entity);
            HttpResponse response = httpClient.execute(request);
            HttpEntity respEntity = response.getEntity();
            if (respEntity != null) {
                json = EntityUtils.toString(respEntity, "UTF-8");
            }
            //  System.out.println(json);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
        }

        return json;
    }

//==================================== END POST ===============================================

}
