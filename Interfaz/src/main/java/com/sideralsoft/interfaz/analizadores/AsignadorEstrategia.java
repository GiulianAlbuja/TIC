package com.sideralsoft.interfaz.analizadores;

import com.sideralsoft.interfaz.analizadores.estrategias.EstrategiaProcesamiento;
import com.sideralsoft.interfaz.analizadores.estrategias.SlaytherStrategy;
import com.sideralsoft.interfaz.analizadores.estrategias.TICStrategy;

public class AsignadorEstrategia {
    private EstrategiaProcesamiento estrategia;

    public EstrategiaProcesamiento asignarEstrategia(String clientAddress){
        switch (clientAddress){
            case "/127.0.0.1":
                estrategia = new TICStrategy();
                System.out.println("Estrategia ASIGNADA TIC");
                return estrategia;
            case "/192.168.100.50":
                estrategia = new SlaytherStrategy();
                System.out.println("Estrategia ASIGNADA SLAYTHER");
                return estrategia;
            default:
                break;
        }
        return null;
    }
}
