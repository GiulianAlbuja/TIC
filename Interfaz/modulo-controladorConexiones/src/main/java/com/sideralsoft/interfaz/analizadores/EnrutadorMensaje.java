package com.sideralsoft.interfaz.analizadores;

import com.sideralsoft.interfaz.comunicadores.Session;
import com.sideralsoft.shared.estrategias.EstrategiaProcesamiento;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class EnrutadorMensaje {
    private Session session;
    private String tipoMensaje;
    private EstrategiaProcesamiento estrategiaProcesamiento;
    private EstadoComunicacion estadoComunicacion;

    public EnrutadorMensaje(Session session) {
        this.session = session;
    }

    public void enrutar(String clientAddress, String mensaje) throws IOException {
        Map<String, String> data = new LinkedHashMap<>();
        AsignadorEstrategia asignadorEstrategia = new AsignadorEstrategia();
        estrategiaProcesamiento = asignadorEstrategia.obtenerEstrategia(mensaje);
        do{
            tipoMensaje = estrategiaProcesamiento.analizarTipoMensaje(mensaje);
            System.out.println("Tipo mensaje" + tipoMensaje);
            switch (tipoMensaje) {
                case "ORU^R01":
                    this.estadoComunicacion = EstadoComunicacion.ENVIO_RESULTADOS;
                    mensaje = estrategiaProcesamiento.validarMensajeORU(clientAddress, mensaje);
                    break;
                case "ACK":
                    if(estadoComunicacion.equals(EstadoComunicacion.ENVIO_RESULTADOS)){
                        session.sendMessage(mensaje);
                    }
                    break;
                case "QRY":
                    this.estadoComunicacion = EstadoComunicacion.CONSULTA_ORDEN;
                    mensaje = estrategiaProcesamiento.validarMensajeQRY(clientAddress, mensaje);
                    break;
                case "QCK":
                    String[] fields = mensaje.split(":::");
                    session.sendMessage(fields [0]);
                    mensaje = fields[1];
                    break;
                case "DSR":
                    session.sendMessage(mensaje);
                    break;
            }
        }while (!tipoMensaje.equals("ACK") && !tipoMensaje.equals("DSR"));
    }
}





