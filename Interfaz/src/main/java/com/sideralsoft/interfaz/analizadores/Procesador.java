package com.sideralsoft.interfaz.analizadores;

import com.sideralsoft.interfaz.analizadores.estrategias.EstrategiaProcesamiento;

public class Procesador {
    private EstrategiaProcesamiento estrategia;

    public void procesar(EstrategiaProcesamiento estrategia, String mensaje) {
        estrategia.procesarMensaje(mensaje);
    }

}
