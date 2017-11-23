package org.ivmlab.proloop.proloop.util;

/**
 * Created by mal21 on 17/08/2016.
 */
import org.ivmlab.proloop.proloop.Splashscreen;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class HttpURLConnectionExample {

    private final String USER_AGENT = "Mozilla/5.0";

    // HTTP GET request
    public void sendGet() throws Exception {

        String username="hitenpratap";
        StringBuilder stringBuilder = new StringBuilder("https://twitter.com/search");
        stringBuilder.append("?q=");
        stringBuilder.append(URLEncoder.encode(username, "UTF-8"));

        URL obj = new URL(stringBuilder.toString());

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Charset", "UTF-8");

        System.out.println("\nSending request to URL : " + obj);
        System.out.println("Response Code : " + con.getResponseCode());
        System.out.println("Response Message : " + con.getResponseMessage());

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String line;
        StringBuffer response = new StringBuffer();

        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        System.out.println(response.toString());

    }

    public void sendPost(String urlPost,String user_id,String pub_id,String cmnt,String DateTime) throws Exception {

        StringBuilder tokenUri=new StringBuilder("user_id=");
        tokenUri.append(URLEncoder.encode(user_id,"UTF-8"));
        tokenUri.append("&pub_id=");
        tokenUri.append(URLEncoder.encode(pub_id,"UTF-8"));
        tokenUri.append("&cmnt=");
        tokenUri.append(URLEncoder.encode(cmnt,"UTF-8"));
        tokenUri.append("&DateTime=");
        tokenUri.append(URLEncoder.encode(DateTime,"UTF-8"));

        String url = urlPost;
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "UTF-8");

        con.setDoOutput(true);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(con.getOutputStream());
        outputStreamWriter.write(tokenUri.toString());
        outputStreamWriter.flush();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        //System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString());

    }

}