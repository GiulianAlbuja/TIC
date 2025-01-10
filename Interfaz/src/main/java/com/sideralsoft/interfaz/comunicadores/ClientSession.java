package com.sideralsoft.interfaz.comunicadores;

import com.sideralsoft.interfaz.analizadores.EnrutadorMensaje;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSession extends Thread {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private TCPServer server;
    private Boolean sesionActiva;
    private EnrutadorMensaje enrutadorMensaje;


    public ClientSession(Socket clientSocket, TCPServer server) {
        this.clientSocket = clientSocket;
        this.server = server;
        this.sesionActiva = true;
        this.enrutadorMensaje = new EnrutadorMensaje();
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            server.notifyClientConnected("Nueva sesión iniciada: " + clientSocket.getInetAddress());

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Mensaje recibido: " + inputLine);
                System.out.println("IP: " + clientSocket.getInetAddress().toString());
                enrutadorMensaje.enrutar(clientSocket.getInetAddress().toString(), inputLine);
                server.notifyMessageReceived("Cliente [" + clientSocket.getInetAddress() + "]: " + inputLine);
            }
        } catch (Exception e) {
            sesionActiva = false;
            server.notifyError("Error en la sesión del cliente: " + e.getMessage());

        } finally {
            closeSession();
        }
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public void closeSession() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null) clientSocket.close();
            sesionActiva = false;
            server.notifyClientConnected("Sesión cerrada para: " + clientSocket.getInetAddress());
        } catch (Exception e) {
            server.notifyError("Error al cerrar la sesión: " + e.getMessage());
        }
    }

    public Boolean estaSesionActiva() {
        return sesionActiva;
    }
}
