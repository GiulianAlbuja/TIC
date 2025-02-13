package com.sideralsoft.interfaz.comunicadores;

import com.sideralsoft.interfaz.componentesUI.ServerListener;
import com.sideralsoft.shared.readers.YamlReader;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class TCPServer extends Thread {
    private static TCPServer instance;
    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private Session session;
    private int puerto;
    private ServerListener listener;

    public TCPServer(int puerto) {
        this.executorService = Executors.newCachedThreadPool();
        this.puerto = puerto;
    }

    public void setListener(ServerListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(puerto);
            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                String clientAddress = clientSocket.getInetAddress().toString();
                System.out.println("Cliente conectado desde: " + clientAddress);
                notifyConnectionStatusChanged("Cliente conectado desde: " + clientAddress);
                this.session = new SessionServidor(clientSocket);
                executorService.execute(session);
            }
        } catch (Exception e) {
            notifyError("Error en el servidor: " + e.getMessage());
        }
    }

    private void notifyMessageReceived(String message) {
        if (listener != null) {
            listener.onMessageReceived(message);
        }
    }

    // Método para notificar que el estado de la conexión cambió
    private void notifyConnectionStatusChanged(String status) {
        if (listener != null) {
            listener.onConnectionStatusChanged(status);
        }
    }

    public void notifyError(String error) {
        if (listener != null) {
            listener.onError(error);
        }
    }

    public void stopServer() {
        try {
            executorService.shutdownNow();

            if (serverSocket != null) serverSocket.close();
            executorService.shutdownNow();
            instance = null;
            session = null;
            notifyConnectionStatusChanged("Servidor detenido.");
            System.out.println("Servidor detenido");
        } catch (Exception e) {
            notifyError("Error al detener el servidor: " + e.getMessage());
        }
    }

    public Session getSession() {
        return session;
    }
}
