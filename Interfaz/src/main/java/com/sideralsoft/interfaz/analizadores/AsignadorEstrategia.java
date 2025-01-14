package com.sideralsoft.interfaz.analizadores;

import com.sideralsoft.interfaz.Entidades.Equipo;
import com.sideralsoft.interfaz.analizadores.estrategias.EstrategiaProcesamiento;
import com.sideralsoft.interfaz.analizadores.estrategias.SlaytherStrategy;
import com.sideralsoft.interfaz.analizadores.estrategias.TICStrategy;
import com.sideralsoft.interfaz.readers.JsonReader;

import java.io.IOException;

public class AsignadorEstrategia {
    private EstrategiaProcesamiento estrategia;

    public EstrategiaProcesamiento asignarEstrategia(String clientAddress) throws IOException {
        JsonReader jsonReader = JsonReader.getInstance();
        Equipo equipo = jsonReader.getEquipoByIp(clientAddress);
        String codigoEquipo = equipo.getCodigoEquipo();
        switch (codigoEquipo){
            case "TIC":
                estrategia = new TICStrategy();
                System.out.println("Estrategia ASIGNADA TIC");
                return estrategia;
            case "TC-220":
                estrategia = new SlaytherStrategy();
                System.out.println("Estrategia ASIGNADA SLAYTHER");
                return estrategia;
            default:
                break;
        }
        return null;
    }
}
