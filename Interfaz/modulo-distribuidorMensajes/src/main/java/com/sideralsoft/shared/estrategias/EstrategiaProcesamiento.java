package com.sideralsoft.shared.estrategias;

import java.io.IOException;
import java.util.Map;

public interface EstrategiaProcesamiento {
    String analizarTipoMensaje(String mensaje);

    String validarMensajeORU(String mensaje) throws IOException;

    Map<String, String> validarMensajeQRY(String mensaje) throws IOException;
}
