package com.sideralsoft.interfaz.interpretadores;

import com.sideralsoft.interfaz.comunicadores.ControladorHTTP;
import com.sideralsoft.interfaz.comunicadores.TCPServer;
import com.sideralsoft.interfaz.interpretadores.estrategias.EstrategiaProcesamiento;

public class EnrutadorMensaje {
    private ControladorHTTP controladorHTTP;
    private Procesador procesador;
    private TCPServer server;
    private String tipoMensaje;
    private EstrategiaProcesamiento estrategiaProcesamiento;

    public EnrutadorMensaje(TCPServer server) {
        //this.controladorHTTP = new ControladorHTTP();
        //this.procesador = new Procesador();
        this.server = server;
    }

    public void enrutar(String clientAddress, String mensaje) {
        //String JSON;
        AsignadorEstrategia asignadorEstrategia = new AsignadorEstrategia();
        estrategiaProcesamiento = asignadorEstrategia.asignarEstrategia(clientAddress);
        tipoMensaje = estrategiaProcesamiento.analizarTipoMensaje(mensaje);
        System.out.println(tipoMensaje);
        switch (tipoMensaje) {
            case "ORU":
                //estrategiaProcesamiento.validarMensaje(clientAddress, mensaje);
                //estrategiaProcesamiento.generarRespuestaConfirmacion(clientAddress, mensaje);
                //JSON = estrategiaProcesamiento.estructurarJSON(clientAddress, mensaje);
                //controladorHTTP.enviarMensajeNube(JSON);
                break;
            case "ACK":
                System.out.println("REGRESA: " + clientAddress +":"+ mensaje);
                server.sendMessageToClient(clientAddress, mensaje);
                break;
        }
    }
}





