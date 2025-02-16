package com.sideralsoft.interfaz;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String args[]) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9090);
        System.out.println("Server is running and waiting for client connection...");

        Socket clientSocket = serverSocket.accept();
        System.out.println("Client connected!");

        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

        String message;
        boolean running = true;

        while (running) {
            // Leer mensaje del cliente
            if (in.ready()) {  // Verifica si hay datos antes de bloquear la lectura
                message = in.readLine();
                if (message == null) break;  // Si el cliente cierra la conexión, salir del bucle
                System.out.println("Client says: " + message);
                out.println("Message received by the server.");
            }

            // Verificar si el servidor debe cerrarse
            if (consoleInput.ready()) {  // Si hay entrada en la consola
                String command = consoleInput.readLine();
                if (command.equalsIgnoreCase("fin")) {
                    System.out.println("Shutting down server...");
                    running = false;
                }
            }
        }

        // Cerrar conexiones
        clientSocket.close();
        serverSocket.close();
        System.out.println("Server stopped.");
    }
}
