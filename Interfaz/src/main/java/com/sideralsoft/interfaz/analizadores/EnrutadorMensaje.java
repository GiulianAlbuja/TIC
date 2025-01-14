package com.sideralsoft.interfaz.analizadores;

import com.sideralsoft.interfaz.comunicadores.TCPServer;
import com.sideralsoft.interfaz.analizadores.estrategias.EstrategiaProcesamiento;

import java.io.IOException;

public class EnrutadorMensaje {
    private TCPServer server;
    private String tipoMensaje;
    private EstrategiaProcesamiento estrategiaProcesamiento;

    public EnrutadorMensaje() {
        this.server = TCPServer.getInstance();
    }

    public void enrutar(String clientAddress, String mensaje) throws IOException {
        AsignadorEstrategia asignadorEstrategia = new AsignadorEstrategia();
        estrategiaProcesamiento = asignadorEstrategia.asignarEstrategia(clientAddress);
        tipoMensaje = estrategiaProcesamiento.analizarTipoMensaje(mensaje);
        System.out.println("Tipo mensaje" + tipoMensaje);
        switch (tipoMensaje) {
            case "ORU^R01":
                estrategiaProcesamiento.validarMensaje(clientAddress, mensaje);
                break;
            case "ACK":
                server.sendMessageToClient(clientAddress, mensaje);
                break;
        }
    }
}





