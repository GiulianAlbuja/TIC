package com.sideralsoft.interfaz.comunicadores;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends TCPActor implements Runnable {
    private ServerSocket serverSocket;
    private int puerto;
    private boolean running;

    public TCPServer(int puerto) {
        super();
        this.puerto = puerto;
    }

    @Override
    public void run() {

        try {
            serverSocket = new ServerSocket(puerto);
            System.out.println("Servidor iniciado en el puerto " + puerto);
            this.running = true;
            Socket clientSocket = null;
            while (!serverSocket.isClosed()) {
                System.out.println("Esperando cliente...");
                clientSocket = serverSocket.accept();

                if (socket != null && !socket.isClosed()) {
                    System.out.println("Un cliente ya está conectado. Rechazando nueva conexión.");
                    clientSocket.close();
                    continue;
                }

                System.out.println("Cliente conectado desde: " + clientSocket.getInetAddress().toString());
                setupConnection(clientSocket);
            }
            clientSocket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            stopServer();
        }
    }

    public void stopServer() {
        this.running = false;
        try {
            closeConnection();
            if (serverSocket != null) serverSocket.close();
            System.out.println("Servidor detenido.");
        } catch (IOException e) {
            System.out.println("Error al detener el servidor: " + e.getMessage());
        }
    }

    public boolean isRunning() {
        return running;
    }
}
