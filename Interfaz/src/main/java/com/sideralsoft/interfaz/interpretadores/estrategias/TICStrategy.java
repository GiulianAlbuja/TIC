package com.sideralsoft.interfaz.interpretadores.estrategias;
public class TICStrategy implements EstrategiaProcesamiento {
    @Override
    public String procesarMensaje(String mensaje) {
        return "ACKTIC";
    }

    @Override
    public String analizarTipoMensaje(String mensaje) {
                return "ACK";
    }

    @Override
    public void validarMensaje(String clientAddress, String mensaje) {
        System.out.println("VALIDAR TIC");
    }

    @Override
    public void generarRespuestaConfirmacion(String clientAddress, String mensaje) {
        System.out.println("Confirmacion TIC");
    }
}
