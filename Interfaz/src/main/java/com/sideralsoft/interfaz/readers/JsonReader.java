package com.sideralsoft.interfaz.readers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sideralsoft.interfaz.Entidades.Equipo;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

public class JsonReader {
    private static JsonReader instance;
    private List<Equipo> equipos;
    private final String rutaArchivoJson = "C:/config/equipos.json";


    private JsonReader() throws IOException {
        try (FileReader reader = new FileReader(rutaArchivoJson)) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Equipo>>() {}.getType();
            equipos = gson.fromJson(reader, listType);
        }
    }

    public static JsonReader getInstance() throws IOException {
        if (instance == null) {
            instance = new JsonReader();
        }
        return instance;
    }

    public Equipo getEquipoByIp(String ip) {
        Optional<Equipo> equipo = equipos.stream().filter(e -> e.getIp().equals(ip)).findFirst();
        return equipo.orElse(null);
    }
}
