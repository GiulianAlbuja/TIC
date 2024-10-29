package com.sideralsoft.interfaz;

import javafx.scene.control.TextArea;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends Thread {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private TextArea messageArea;

    public TCPServer(TextArea messageArea) {
        this.messageArea = messageArea;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(3001); // Puerto del servidor
            messageArea.appendText("Servidor iniciado en el puerto 3001\n");

            clientSocket = serverSocket.accept();
            messageArea.appendText("Cliente conectado: " + clientSocket.getInetAddress() + "\n");

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                messageArea.appendText("Cliente: " + inputLine + "\n");
            }
        } catch (Exception e) {
            messageArea.appendText("Error en el servidor: " + e.getMessage() + "\n");
        }
    }

    public void sendMessageToClient(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public void stopServer() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null) clientSocket.close();
            if (serverSocket != null) serverSocket.close();
            messageArea.appendText("Servidor detenido\n");
        } catch (Exception e) {
            messageArea.appendText("Error al detener el servidor: " + e.getMessage() + "\n");
        }
    }
}
