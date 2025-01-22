package com.sideralsoft.interfaz.comunicadores;

import com.sideralsoft.interfaz.analizadores.EnrutadorMensaje;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Session extends Thread {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private Boolean sesionActiva;
    private EnrutadorMensaje enrutadorMensaje;
    private static List<String> mensajesRecibidos;
    private static List<String> mensajesEnviados;
    private String tipoSession;


    public Session(Socket clientSocket, String tipoSession) {
        this.clientSocket = clientSocket;
        this.sesionActiva = true;
        this.enrutadorMensaje = new EnrutadorMensaje(this);
        mensajesEnviados = new ArrayList<>();
        mensajesRecibidos = new ArrayList<>();
        this.tipoSession = tipoSession;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("Nueva sesión iniciada: " + clientSocket.getInetAddress().getHostAddress());

            if(tipoSession.equals("servidor")){
                System.out.println("ACTUANDO COMO CLIENTE");
                actuarComoCliente();
            }else{
                System.out.println("ACTUANDO COMO SERVIDOR");
                actuarComoServidor();
            }

        } catch (Exception e) {
            sesionActiva = false;
            System.out.println("Error en la sesión del cliente: " + e.getMessage());

        } finally {
            closeSession();
        }
    }

    private void actuarComoServidor() throws IOException {
        String inputLine;
        while ((inputLine = in.readLine()) != null ) {
            if (inputLine.trim().isEmpty()) {
                System.out.println("Línea vacía recibida. Ignorando...");
                continue;
            }
            System.out.println("Mensaje recibido: " + inputLine);
            String prueba = "/";
            prueba = prueba + clientSocket.getInetAddress().getHostAddress().toString();
            System.out.println("PRUEBA: " + prueba);
            System.out.println("IP: " + clientSocket.getInetAddress().toString());
            //enrutadorMensaje.enrutar(clientSocket.getInetAddress().toString(), inputLine);
            enrutadorMensaje.enrutar(prueba, inputLine);
            System.out.println("Cliente [" + clientSocket.getInetAddress() + "]: " + inputLine);
            mensajesRecibidos.add(inputLine);
        }
    }

    private void actuarComoCliente() throws IOException {
        String inputLine;
        inputLine = in.readLine();
        System.out.println("Mensaje recibido: " + inputLine);
        String prueba = "/";
        prueba = prueba + clientSocket.getInetAddress().getHostAddress().toString();
        System.out.println("PRUEBA: " + prueba);
        System.out.println("IP: " + clientSocket.getInetAddress().toString());
        enrutadorMensaje.enrutar(prueba, inputLine);
        System.out.println("Cliente [" + clientSocket.getInetAddress() + "]: " + inputLine);
        mensajesRecibidos.add(inputLine);
    }

    public void sendMessage(String message) {
        mensajesEnviados.add(message);
        if (out != null) {
            out.println(message);
        }
    }

    public List<String> getMensajesRecibidos() {
        return mensajesRecibidos;
    }
    public List<String> getMensajesEnviados() {
        return mensajesEnviados;
    }

    public void closeSession() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null) clientSocket.close();
            sesionActiva = false;
            System.out.println("Sesión cerrada para: " + clientSocket.getInetAddress());
        } catch (Exception e) {
            System.out.println("Error al cerrar la sesión: " + e.getMessage());
        }
    }


    public Boolean estaSesionActiva() {
        return sesionActiva;
    }
}
