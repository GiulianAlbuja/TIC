package com.sideralsoft.interfaz.interpretadores;

import com.sideralsoft.interfaz.interpretadores.estrategias.EstrategiaProcesamiento;
import com.sideralsoft.interfaz.interpretadores.estrategias.SlaytherStrategy;
import com.sideralsoft.interfaz.interpretadores.estrategias.TICStrategy;

public class AsignadorEstrategia {
    private EstrategiaProcesamiento estrategia;

    public EstrategiaProcesamiento asignarEstrategia(String clientAddress){
        switch (clientAddress){
            case "/127.0.0.1":
                estrategia = new TICStrategy();
                System.out.println("Estrategia ASIGNADA TIC");
                return estrategia;
            case "/172.29.81.230":
                estrategia = new SlaytherStrategy();
                System.out.println("Estrategia ASIGNADA SLAYTHER");
                return estrategia;
            default:
                break;
        }
        return null;
    }
}
