package com.asparagus.usclassifieds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

//handles REST endpoints
public class HttpDataHandler {

    public HttpDataHandler() {

    }

    public String getHTTPData(String requestURL)
    {
        URL url;
        String response = "";
        try{
            url = new URL(requestURL);
            System.out.println("url details " + url);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

            int responseCode = conn.getResponseCode();
            System.out.println("connection details " + conn.getResponseCode());
            if(responseCode == HttpsURLConnection.HTTP_OK)
            {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while((line = br.readLine()) != null)
                    response+=line;
            }
            else
                response = "";

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}