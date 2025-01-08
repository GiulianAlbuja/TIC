package com.sideralsoft.interfaz.comunicadores;

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
    private Map<String, ClientSession> sessions;
    private List<ServerListener> listeners;
    private List<String> mensajesRecibidos = new ArrayList<>();

    private TCPServer() {
        this.executorService = Executors.newCachedThreadPool();
        this.sessions = new HashMap<>();
        this.listeners = new ArrayList<>();
    }

    public static synchronized TCPServer getInstance() {
        if (instance == null) {
            instance = new TCPServer();
        }
        return instance;
    }


    public void addServerListener(ServerListener listener) {
        listeners.add(listener);
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(3001);
            notifyClientConnected("Servidor iniciado en el puerto 3001");

            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                String clientAddress = clientSocket.getInetAddress().toString();
                notifyClientConnected("Cliente conectado desde: " + clientAddress);

                ClientSession clientSession = new ClientSession(clientSocket, this);
                sessions.put(clientAddress, clientSession);
                executorService.execute(clientSession);
            }
        } catch (Exception e) {
            notifyError("Error en el servidor: " + e.getMessage());
        }
    }

    public void notifyMessageReceived(String message) {
        mensajesRecibidos.add(message);
        for (ServerListener listener : listeners) {
            listener.onMessageReceived(message);
        }
    }

    public List<String> getMensajesRecibidos() {
        return mensajesRecibidos;
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


    public void sendMessageToClient(String clientAddress, String message) {
        ClientSession clientSession = sessions.get(clientAddress);
        System.out.println("CLIENTE: " + clientSession + ":" + clientAddress);
        if (clientSession != null) {
            clientSession.sendMessage(message);
            System.out.println("MENSAJE ACK: " + message);
        } else {
            notifyError("No se encontró al cliente con dirección: " + clientAddress);
        }
    }

    public void stopServer() {
        try {
            executorService.shutdownNow();
            if (serverSocket != null) serverSocket.close();
            notifyClientConnected("Servidor detenido.");
            System.out.println("DETENIDOOOO");
        } catch (Exception e) {
            notifyError("Error al detener el servidor: " + e.getMessage());
        }
    }


}
