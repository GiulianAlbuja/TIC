package com.sideralsoft.interfaz.comunicadores;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPClient {
    private Socket socket;
    private Session session;
    private ExecutorService executorService;

    public TCPClient(String ip, int port) {
        try {
            this.executorService = Executors.newCachedThreadPool();
            socket = new Socket(ip, port);
            System.out.println("Conectado a: " + socket.getInetAddress().toString() + " en el puerto " + port);
            this.session = new SessionCliente(socket);
            executorService.execute(session);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Session getSession() {
        return session;
    }


}
