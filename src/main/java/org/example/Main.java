package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;


import java.io.*;
import java.util.Arrays;

public class Main {
    public static final String REMOTE_SERVICE_URI = "https://api.nasa.gov/planetary/apod?api_key=WfQnVgoSO6fdvnFgh9dfSqt2H6bc1EPDNdx8r5Kb";
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        HttpGet request = new HttpGet(REMOTE_SERVICE_URI);
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());

        CloseableHttpResponse response = httpClient.execute(request);
        Arrays.stream(response.getAllHeaders()).forEach(System.out::println);

        NASA nasa = mapper.readValue(response.getEntity().getContent(), NASA.class);
        System.out.println(nasa);

        HttpGet request2 = new HttpGet(nasa.getUrl());
        request2.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
        CloseableHttpResponse response2 = httpClient.execute(request2);
        Arrays.stream(response2.getAllHeaders()).forEach(System.out::println);

        String file = nasa.getUrl();
        try (FileOutputStream fos = new FileOutputStream("/Users/vi/Desktop/ABELL1060_LRGB_NASA.jpg")) {
            byte[] bytes = file.getBytes();
            fos.write(bytes, 0, bytes.length);
            fos.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}