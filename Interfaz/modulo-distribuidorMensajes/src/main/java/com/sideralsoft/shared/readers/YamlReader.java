package com.sideralsoft.shared.readers;

import org.yaml.snakeyaml.Yaml;
import com.sideralsoft.shared.entidades.Equipo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class YamlReader {
    private static YamlReader instance;
    private Map<String, Equipo> equipos;
    private final String rutaArchivoYaml = "C:/config/equipos.yaml";

    private YamlReader() throws IOException {
        try (FileInputStream inputStream = new FileInputStream(rutaArchivoYaml)) {
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(inputStream);
            if (data.containsKey("equipos")) {
                equipos = ((Map<String, Map<String, Object>>) data.get("equipos"))
                        .entrySet()
                        .stream()
                        .collect(java.util.stream.Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> mapToEquipo(entry.getKey(), entry.getValue())
                        ));
            }
        }
    }

    public static YamlReader getInstance() throws IOException {
        if (instance == null) {
            instance = new YamlReader();
        }
        return instance;
    }

    public Equipo getEquipoByIp(String ip) {
        Optional<Equipo> equipo = equipos.values().stream().filter(e -> e.getIp().equals(ip)).findFirst();
        return equipo.orElse(null);
    }

    public Equipo getEquipoByConfiguracionHl7(String configuracionHl7) {
        Optional<Equipo> equipo = equipos.values().stream().filter(e -> e.getConfiguracionHl7().equals(configuracionHl7)).findFirst();
        return equipo.orElse(null);
    }

    public Equipo getEquipoByNombre(String nombre) {
        return equipos.getOrDefault(nombre, equipos.get("Default"));
    }

    private Equipo mapToEquipo(String nombre, Map<String, Object> data) {
        return new Equipo(
                nombre,
                (String) data.get("id"),
                (String) data.get("ip"),
                (int) data.get("puerto"),
                (String) data.get("configuracion_hl7"),
                (String) data.get("token"),
                (java.util.List<String>) data.get("campos_identificadores")
        );
    }

    public Map<String, Equipo> getEquipos() {
        return equipos;
    }
}
