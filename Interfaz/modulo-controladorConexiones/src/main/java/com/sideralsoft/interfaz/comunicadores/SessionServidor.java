package com.sideralsoft.interfaz.comunicadores;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SessionServidor extends Session{
    public SessionServidor(Socket socket) {
        super(socket);
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Nueva sesión iniciada en servidor: " + socket.getInetAddress().getHostAddress());

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.trim().isEmpty()) {
                    System.out.println("Línea vacía recibida. Ignorando...");
                    continue;
                }
                System.out.println("Mensaje recibido: " + inputLine);
                String prueba = "/";
                prueba = prueba + socket.getInetAddress().getHostAddress();
                System.out.println("PRUEBA: " + prueba);
                System.out.println("IP: " + socket.getInetAddress().toString());
                enrutadorMensaje.enrutar(prueba, inputLine);
                System.out.println("Cliente [" + socket.getInetAddress() + "]: " + inputLine);
                mensajesRecibidos.add(inputLine);
            }
        } catch (IOException e) {
            sesionActiva = false;
            System.out.println("Error en la sesión del servidor: " + e.getMessage());
        } finally {
            closeSession();
        }
    }
}
