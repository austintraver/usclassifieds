package com.asparagus.usclassifieds;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

// Handles REST endpoints
class HttpDataHandler {

    private String TAG = HttpDataHandler.class.getSimpleName();

    HttpDataHandler() {}

    String getHTTPData(String requestURL) {
        URL url;
        StringBuilder response = new StringBuilder();
        try {
            url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }
        } catch (ProtocolException pe) {
            Log.d(TAG, Objects.requireNonNull(pe.getLocalizedMessage()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }
}