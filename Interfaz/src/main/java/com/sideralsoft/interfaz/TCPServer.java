package com.sideralsoft.interfaz;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServer extends Thread {
    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private Map<String, ClientSession> sessions;  // Mapa para almacenar sesiones por dirección de cliente
    private List<ServerListener> listeners;  // Lista de listeners para notificar eventos
    private List<String> mensajesRecibidos = new ArrayList<>();



    public List<String> getMensajesRecibidos() {
        return mensajesRecibidos;
    }

    public TCPServer() {
        this.executorService = Executors.newCachedThreadPool();
        this.sessions = new HashMap<>();
        this.listeners = new ArrayList<>();
    }

    // Agregar un listener al servidor
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

                // Crear una nueva sesión y agregarla al mapa
                ClientSession clientSession = new ClientSession(clientSocket, this);
                sessions.put(clientAddress, clientSession);
                executorService.execute(clientSession);
            }
        } catch (Exception e) {
            notifyError("Error en el servidor: " + e.getMessage());
        }
    }

    // Notificar que se recibió un mensaje
    public void notifyMessageReceived(String message) {
        mensajesRecibidos.add(message);  // Guardar el mensaje recibido
        for (ServerListener listener : listeners) {
            listener.onMessageReceived(message);
        }
    }

    // Notificar que un cliente se conectó
    public void notifyClientConnected(String clientInfo) {
        for (ServerListener listener : listeners) {
            listener.onClientConnected(clientInfo);
        }
    }

    // Notificar un error
    public void notifyError(String error) {
        for (ServerListener listener : listeners) {
            listener.onError(error);
        }
    }

    // Enviar mensaje a un cliente específico
    public void sendMessageToClient(String clientAddress, String message) {
        ClientSession clientSession = sessions.get(clientAddress);
        if (clientSession != null) {
            clientSession.sendMessage(message);
        } else {
            notifyError("No se encontró al cliente con dirección: " + clientAddress);
        }
    }

    public void stopServer() {
        try {
            executorService.shutdownNow();
            if (serverSocket != null) serverSocket.close();
            notifyClientConnected("Servidor detenido.");
        } catch (Exception e) {
            notifyError("Error al detener el servidor: " + e.getMessage());
        }
    }
}
