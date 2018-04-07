package eco.org.greenapp.eco.org.greenapp;

import android.support.annotation.NonNull;

import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.io.UnsupportedEncodingException;


import javax.net.ssl.HttpsURLConnection;


public class ExecuteRequests {

    public String sendPostRequest(String requestURL, HashMap<String, String> data) {

        StringBuilder stringBuilder = new StringBuilder();
        URL url;

        try {
            url = new URL(requestURL);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            bufferedWriter.write(getResultData(data));

            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                stringBuilder = new StringBuilder();
                String response;
                while ((response = br.readLine()) != null){
                    stringBuilder.append(response);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    @NonNull
    private String getResultData(HashMap<String, String> parametri) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : parametri.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}