package com.hbcd.service.HTTP;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HTTPRequest {
    private static final String ServiceEndPoint = "http://www.yahoo.com";

    public static void post() throws Exception {

        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(ServiceEndPoint);

            // add header
            post.setHeader("User-Agent", "USER_AGENT");

            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("sn", "C02G8416DRJM"));
            urlParameters.add(new BasicNameValuePair("cn", ""));
            urlParameters.add(new BasicNameValuePair("locale", ""));
            urlParameters.add(new BasicNameValuePair("caller", ""));
            urlParameters.add(new BasicNameValuePair("num", "12345"));

            post.setEntity(new UrlEncodedFormEntity(urlParameters));

            HttpResponse response = client.execute(post);
            System.out.println("Response Code : "
                    + response.getStatusLine().getStatusCode());

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//	public static String post(String url, List<KeyValuePair> headerList, String jsonRequest)
//	{
//		String rtrn = "";
//		
//		//Ensure Not NULL
//		if (headerList == null) headerList = new ArrayList<KeyValuePair>();
//		
//		try (CloseableHttpClient httpClient = HttpClientBuilder.create().getServiceExcelMapping()) {
//            HttpPost request = new HttpPost(url);
//            StringEntity entity  = new StringEntity(jsonRequest, "UTF-8");
//            request.addHeader("content-type", "application/json");
//            for (KeyValuePair kv : headerList)
//            {
//            	request.addHeader(kv.getKey(), kv.getValue());
//            }
//            request.setEntity(entity);
//            HttpResponse response = httpClient.execute(request);
//            HttpEntity respEntity = response.getEntity();
//            if (respEntity != null)
//            {
//            	rtrn = EntityUtils.toString(respEntity, "UTF-8");
//            }
//          //  System.out.println(json);
//
//        } catch (IOException ex) {
//        	ex.printStackTrace();
//        }
//		finally
//		{
//		}
//		
//		return rtrn;
//	}

    //WORKING
    public static String get(String servicUrl) {
        String rtrn = "";

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet(servicUrl);
            request.addHeader("content-type", "application/html");
            HttpResponse result = httpClient.execute(request);
            rtrn = EntityUtils.toString(result.getEntity(), "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return rtrn;
    }

}
