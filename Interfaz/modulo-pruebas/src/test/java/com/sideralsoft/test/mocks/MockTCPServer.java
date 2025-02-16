package com.sideralsoft.test.mocks;

import com.sideralsoft.interfaz.comunicadores.TCPServer;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MockTCPServer extends Thread {
    private static MockTCPServer instance;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private boolean running = true;
    private int port;

    private ExecutorService executorService;

    private MockTCPServer(int port) {
        this.port = port;
        this.executorService = Executors.newCachedThreadPool();
    }

    public static synchronized MockTCPServer getInstance() {
        if (instance == null) {
            instance = new MockTCPServer(3002);
        }
        return instance;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Servidor mock TCP iniciado en el puerto " + port);
            while (running) {
                try {
                    this.clientSocket = serverSocket.accept();
                    System.out.println("Cliente conectado: " + clientSocket.getInetAddress());
                } catch (SocketException e) {
                    if (!running) {
                        System.out.println("Servidor Mock detenido.");
                        break;
                    }
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            if (!running) {
                System.out.println("Servidor detenido correctamente.");
            } else {
                e.printStackTrace();
            }
        }
    }

    public void stopServer() {
        running = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
            executorService.shutdownNow();
            instance = null;
            System.out.println("Servidor MOCK detenido.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getClientSocket() {
        return clientSocket;
    }
}
