package com.sideralsoft.shared.readers;

import com.sideralsoft.shared.entidades.Equipo;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            YamlReader yamlReader = YamlReader.getInstance();

            // Buscar equipo por IP
            Equipo equipoPorIp = yamlReader.getEquipoByIp("/127.0.0.1");
            System.out.println("Equipo encontrado por IP: " + equipoPorIp);
            System.out.println(equipoPorIp.getNombre());

            // Buscar equipo por nombre
            Equipo equipoPorNombre = yamlReader.getEquipoByNombre("AnalyzerA");
            System.out.println("Equipo encontrado por nombre: " + equipoPorNombre);
            System.out.println(equipoPorNombre.getNombre());

            // Buscar un equipo que no existe (usará Default)
            Equipo equipoNoExistente = yamlReader.getEquipoByNombre("NoExiste");
            System.out.println("Equipo por nombre desconocido (usando Default): " + equipoNoExistente);
            System.out.println(equipoNoExistente.getNombre());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
