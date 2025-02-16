package com.sideralsoft.interfaz.comunicadores;

import java.io.IOException;
import java.net.Socket;

public class TCPClient extends TCPActor {

    public TCPClient(String ip, int port) {
        try {
            Socket socket = new Socket(ip, port);
            System.out.println("Conectado a: " + socket.getInetAddress().toString() + " en el puerto " + port);
            setupConnection(socket);
        } catch (IOException e) {
            System.out.println("Error al conectar con el servidor: " + e.getMessage());
        }
    }

    @Override
    protected void handleMessage(String message) {
        mensajesRecibidos.add(message);
        System.out.println("Mensaje recibido del servidor: " + message);
    }
}
