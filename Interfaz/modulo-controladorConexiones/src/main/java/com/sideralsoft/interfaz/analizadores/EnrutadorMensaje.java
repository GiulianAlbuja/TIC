package com.sideralsoft.interfaz.analizadores;

import com.sideralsoft.interfaz.comunicadores.Session;
import com.sideralsoft.shared.estrategias.EstrategiaProcesamiento;

import java.io.IOException;

public class EnrutadorMensaje {
    private Session session;
    private String tipoMensaje;
    private EstrategiaProcesamiento estrategiaProcesamiento;

    public EnrutadorMensaje(Session session) {
        this.session = session;
    }

    public void enrutar(String clientAddress, String mensaje) throws IOException {
        //ENUMERACION
        //VARIABLE COMPORTAMIENTO
        AsignadorEstrategia asignadorEstrategia = new AsignadorEstrategia();
        estrategiaProcesamiento = asignadorEstrategia.obtenerEstrategia(mensaje);
        do{
            tipoMensaje = estrategiaProcesamiento.analizarTipoMensaje(mensaje);
            System.out.println("Tipo mensaje" + tipoMensaje);
            switch (tipoMensaje) {
                case "ORU^R01":
                    //pongo variable o enumeracion en un modo
                    mensaje = estrategiaProcesamiento.validarMensaje(clientAddress, mensaje);

                    //estrategiaProcesamiento.validarMensaje(clientAddress, mensaje);
                    break;
                case "ACK":
                    session.sendMessage(mensaje);
                    break;
                case "Q":
                    //pongo variable o enumeracion en un modo
                    break;
            }
        }while (!tipoMensaje.equals("ACK"));
        //while (!tipoMensaje.equals("ACK") && variableEnumeracion.equals("envio"));
    }
}





