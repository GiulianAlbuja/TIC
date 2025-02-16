package com.sideralsoft.interfaz.comunicadores;

import com.sideralsoft.interfaz.analizadores.EnrutadorMensaje;
import com.sideralsoft.interfaz.componentesUI.TCPListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class TCPActor {
    protected Socket socket;
    protected PrintWriter out;
    protected BufferedReader in;
    protected ExecutorService executorService;
    protected EnrutadorMensaje enrutadorMensaje;
    protected static List<String> mensajesRecibidos;
    protected static List<String> mensajesEnviados;
    private TCPListener listener;

    public void setListener(TCPListener listener) {
        this.listener = listener;
    }

    public TCPActor() {
        this.executorService = Executors.newCachedThreadPool();
        mensajesEnviados = new ArrayList<>();
        mensajesRecibidos = new ArrayList<>();
        this.enrutadorMensaje = new EnrutadorMensaje(this);

    }


    public List<String> getMensajesRecibidos() {
        return mensajesRecibidos;
    }
    public List<String> getMensajesEnviados() {
        return mensajesEnviados;
    }

    protected void setupConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        executorService.execute(this::listenForMessages);
    }

    public void sendMessage(String message) {
        mensajesEnviados.add(message);
        if (out != null) {
            out.println(message);
        }
        if (listener != null) {
            listener.updateSentMessage(message);
        }
    }

    protected void listenForMessages() {
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.trim().isEmpty()) {
                    continue;
                }
                handleMessage(inputLine);
                enrutadorMensaje.enrutar(inputLine);
            }
        } catch (IOException e) {
            System.out.println("Error en la comunicación: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    public void handleMessage(String message){
        mensajesRecibidos.add(message);
        if (listener != null) {
            listener.updateReceivedMessage(message);
        }
    };

    public void closeConnection() {

        try {
            if (executorService != null) {
                executorService.shutdownNow(); // Asegurar que los hilos se detienen
            }
            if (in != null) {
                in = null;
            }
            if (out != null) {
                out = null;
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
                socket = null;
            }
            System.out.println("Conexión cerrada.");
        } catch (IOException e) {
            System.out.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}
