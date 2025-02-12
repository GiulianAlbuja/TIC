package com.sideralsoft.interfaz.comunicadores;

import com.sideralsoft.interfaz.analizadores.EnrutadorMensaje;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public abstract class Session extends Thread {
    protected Socket socket;
    protected PrintWriter out;
    protected BufferedReader in;
    protected Boolean sesionActiva;
    protected EnrutadorMensaje enrutadorMensaje;
    protected static List<String> mensajesRecibidos;
    protected static List<String> mensajesEnviados;



    public Session(Socket socket) {
        this.socket = socket;
        this.sesionActiva = true;
        this.enrutadorMensaje = new EnrutadorMensaje(this);
        mensajesEnviados = new ArrayList<>();
        mensajesRecibidos = new ArrayList<>();
    }

    @Override
    public abstract void run();

    public void sendMessage(String message) {
        mensajesEnviados.add(message);
        if (out != null) {
            out.println(message);
        }
    }

    public List<String> getMensajesRecibidos() {
        return mensajesRecibidos;
    }
    public List<String> getMensajesEnviados() {
        return mensajesEnviados;
    }

    public void closeSession() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
            sesionActiva = false;
            System.out.println("Sesión cerrada para: " + socket.getInetAddress());
        } catch (Exception e) {
            System.out.println("Error al cerrar la sesión: " + e.getMessage());
        }
    }

    public Boolean estaSesionActiva() {
        return sesionActiva;
    }
}
