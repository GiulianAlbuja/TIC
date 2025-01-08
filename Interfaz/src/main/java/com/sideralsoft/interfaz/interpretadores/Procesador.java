package com.sideralsoft.interfaz.interpretadores;

import com.sideralsoft.interfaz.interpretadores.estrategias.EstrategiaProcesamiento;

public class Procesador {
    private EstrategiaProcesamiento estrategia;

    public void procesar(EstrategiaProcesamiento estrategia, String mensaje) {
        estrategia.procesarMensaje(mensaje);
    }

}
