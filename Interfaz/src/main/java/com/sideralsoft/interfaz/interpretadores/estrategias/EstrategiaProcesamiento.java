package com.sideralsoft.interfaz.interpretadores.estrategias;

public interface EstrategiaProcesamiento {
    String procesarMensaje(String mensaje);

    String analizarTipoMensaje(String mensaje);

    void validarMensaje(String clientAddress, String mensaje);

    void generarRespuestaConfirmacion(String clientAddress, String mensaje);
}
