package com.sideralsoft.interfaz.comunicadores;

import java.util.HashMap;
import java.util.Map;

public class TCPServerController {
    private static TCPServerController instance;
    private final Map<String, TCPServer> servidores;

    private TCPServerController() {
        servidores = new HashMap<>();
    }

    public static synchronized TCPServerController getInstance() {
        if (instance == null) {
            instance = new TCPServerController();
        }
        return instance;
    }

    public synchronized void startServer(String equipo, int puerto) {
        if (servidores.containsKey(equipo) && servidores.get(equipo).isRunning()) {
            System.out.println("El servidor para " + equipo + " ya está en ejecución.");
            return;
        }

        TCPServer servidor = new TCPServer(puerto);
        new Thread(servidor).start();
        servidores.put(equipo, servidor);
        System.out.println("Servidor iniciado para " + equipo + " en el puerto " + puerto);
    }

    public synchronized void stopServer(String equipo) {
        if (servidores.containsKey(equipo)) {
            servidores.get(equipo).stopServer();
            servidores.remove(equipo);
            System.out.println("Servidor detenido para " + equipo);
        }
    }

    public synchronized boolean isServerRunning(String equipo) {
        return servidores.containsKey(equipo) && servidores.get(equipo).isRunning();
    }
}
