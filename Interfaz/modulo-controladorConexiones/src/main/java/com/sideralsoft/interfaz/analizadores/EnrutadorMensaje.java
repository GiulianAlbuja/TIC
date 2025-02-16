package com.sideralsoft.interfaz.analizadores;

import com.sideralsoft.interfaz.comunicadores.TCPActor;
import com.sideralsoft.shared.estrategias.EstrategiaProcesamiento;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class EnrutadorMensaje {
    private TCPActor tcpActor;
    private String tipoMensaje;
    private EstrategiaProcesamiento estrategiaProcesamiento;
    private EstadoComunicacion estadoComunicacion;

    public EnrutadorMensaje(TCPActor tcpActor) {
        this.tcpActor = tcpActor;
    }

    public void enrutar(String mensaje) throws IOException {
        Map<String, String> data = new LinkedHashMap<>();
        AsignadorEstrategia asignadorEstrategia = new AsignadorEstrategia();
        estrategiaProcesamiento = asignadorEstrategia.obtenerEstrategia(mensaje);
        do{
            tipoMensaje = estrategiaProcesamiento.analizarTipoMensaje(mensaje);
            System.out.println("Tipo mensaje" + tipoMensaje);
            switch (tipoMensaje) {
                case "ORU^R01":
                    this.estadoComunicacion = EstadoComunicacion.ENVIO_RESULTADOS;
                    mensaje = estrategiaProcesamiento.validarMensajeORU(mensaje);
                    break;
                case "ACK":
                    if(estadoComunicacion.equals(EstadoComunicacion.ENVIO_RESULTADOS)){
                        tcpActor.sendMessage(mensaje);
                    }
                    break;
                case "QRY":
                    this.estadoComunicacion = EstadoComunicacion.CONSULTA_ORDEN;
                    data = estrategiaProcesamiento.validarMensajeQRY(mensaje);
                    mensaje = data.get("QCK");
                    break;
                case "QCK":
                    tcpActor.sendMessage(mensaje);
                    mensaje = data.get("DSR");
                    break;
                case "DSR":
                    tcpActor.sendMessage(mensaje);
                    break;
            }
        }while (!tipoMensaje.equals("ACK") && !tipoMensaje.equals("DSR") && tipoMensaje != null);
    }
}





