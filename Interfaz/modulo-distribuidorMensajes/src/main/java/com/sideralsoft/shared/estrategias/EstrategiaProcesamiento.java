package com.sideralsoft.shared.estrategias;

import java.io.IOException;

public interface EstrategiaProcesamiento {
    String analizarTipoMensaje(String mensaje);

    String validarMensajeORU(String clientAddress, String mensaje) throws IOException;

    String validarMensajeQRY(String clientAddress, String mensaje) throws IOException;
}
