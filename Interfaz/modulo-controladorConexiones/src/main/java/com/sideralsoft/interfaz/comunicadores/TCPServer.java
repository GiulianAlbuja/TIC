package com.sideralsoft.interfaz.comunicadores;

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
    private List<ServerListener> listeners;
    private String puerto;

    public TCPServer(String puerto) {
        this.executorService = Executors.newCachedThreadPool();
        this.listeners = new ArrayList<>();
        this.puerto = puerto;
    }


    public void addServerListener(ServerListener listener) {
        listeners.add(listener);
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(Integer.parseInt(puerto));
            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                String clientAddress = clientSocket.getInetAddress().toString();
                System.out.println("Cliente conectado desde: " + clientAddress);
                this.session = new SessionServidor(clientSocket);
                executorService.execute(session);
            }
        } catch (Exception e) {
            notifyError("Error en el servidor: " + e.getMessage());
        }
    }

    public void notifyMessageReceived(String message) {
        for (ServerListener listener : listeners) {
            listener.onMessageReceived(message);
        }
    }

    public void notifyClientConnected(String clientInfo) {
        for (ServerListener listener : listeners) {
            listener.onClientConnected(clientInfo);
        }
    }

    public void notifyError(String error) {
        for (ServerListener listener : listeners) {
            listener.onError(error);
        }
    }

    public void stopServer() {
        try {
            executorService.shutdownNow();

            if (serverSocket != null) serverSocket.close();
            executorService.shutdownNow();
            instance = null;
            notifyClientConnected("Servidor detenido.");
            System.out.println("Servidor detenido");
        } catch (Exception e) {
            notifyError("Error al detener el servidor: " + e.getMessage());
        }
    }

    public Session getSession() {
        return session;
    }
}
