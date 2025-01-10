package com.sideralsoft.interfaz.analizadores.estrategias;

public interface EstrategiaProcesamiento {
    String procesarMensaje(String mensaje);

    String analizarTipoMensaje(String mensaje);

    void validarMensaje(String clientAddress, String mensaje);
}
