package com.sideralsoft.interfaz.comunicadores;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class ControladorHTTP {

    public void enviarMensajeNube(String json){
        int responseCode = 0;
        StringBuilder responseContent = new StringBuilder();
        try {
            //PREGUNTAR
            URL url = new URL("http://localhost:8000/api/resultados");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = json.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            responseCode = connection.getResponseCode();

            System.out.println("Response code: " + responseCode);
            InputStream inputStream;
            if (responseCode >= 200 && responseCode < 300) {
                inputStream = connection.getInputStream();
            } else {
                inputStream = connection.getErrorStream();
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                responseContent.append(reader.lines().collect(Collectors.joining("\n")));
            }

            System.out.println("Response body:" + responseContent.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(responseCode);
    }

}
