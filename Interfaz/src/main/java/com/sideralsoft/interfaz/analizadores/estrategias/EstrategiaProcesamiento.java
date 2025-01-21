package com.sideralsoft.interfaz.analizadores.estrategias;

import java.io.IOException;

public interface EstrategiaProcesamiento {
    String procesarMensaje(String mensaje);

    String analizarTipoMensaje(String mensaje);

    String validarMensaje(String clientAddress, String mensaje) throws IOException;
}
