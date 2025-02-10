package com.sideralsoft.shared.comunicadores;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ControladorHTTP {
    private static List<String> mensajesEnviados;
    private static ControladorHTTP instance;

    private ControladorHTTP() {
        mensajesEnviados = new ArrayList<>();
    }

    public static synchronized ControladorHTTP getInstance() {
        if (instance == null) {
            instance = new ControladorHTTP();
        }
        return instance;
    }

    public void enviarResultadosClinicosANube(String json){
        int responseCode = 0;
        StringBuilder responseContent = new StringBuilder();
        try {
            URL url = new URL("http://localhost:8000/api/resultados");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = json.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
                registrarMensaje(json);
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

    public StringBuilder enviarConsultaDeOrdenANube(String json) {
        int responseCode = 0;
        StringBuilder responseContent = new StringBuilder();
        try {
            URL url = new URL("http://localhost:8000/api/ordenes");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = json.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
                registrarMensaje(json);
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
        return responseContent;

    }



    private void registrarMensaje(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = objectMapper.readValue(json, Map.class);
        if (jsonMap.containsKey("hl7Trama") && jsonMap.get("hl7Trama") != null && !jsonMap.get("hl7Trama").toString().trim().isEmpty()) {
            jsonMap.put("hl7Trama", "hl7Trama");
        }
        if (jsonMap.containsKey("token") && jsonMap.get("token") != null && !jsonMap.get("token").toString().trim().isEmpty()) {
            jsonMap.put("token", "token");
        }
        String jsonModificado = objectMapper.writeValueAsString(jsonMap);
        mensajesEnviados.add(jsonModificado);
    }

    public List<String> getMensajesEnviados() {
        return mensajesEnviados;
    }


}
