package com.sideralsoft.shared.estrategias;

import java.io.IOException;

public interface EstrategiaProcesamiento {
    String analizarTipoMensaje(String mensaje);

    String validarMensaje(String clientAddress, String mensaje) throws IOException;
}
