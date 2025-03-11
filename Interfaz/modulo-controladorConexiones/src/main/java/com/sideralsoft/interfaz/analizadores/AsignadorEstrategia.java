package com.sideralsoft.interfaz.analizadores;

import com.sideralsoft.shared.entidades.Equipo;
import com.sideralsoft.shared.estrategias.EstrategiaProcesamiento;
import com.sideralsoft.shared.readers.YamlReader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;

public class AsignadorEstrategia {
    private EstrategiaProcesamiento estrategia;
    private String nombreEstrategia;

    public EstrategiaProcesamiento obtenerEstrategia(String mensaje) throws IOException {
        nombreEstrategia = identificarEstrategia(mensaje);
        String jarDirectoryPath = "C:\\instalaciones\\interfaz-hl7\\lib";
        System.out.println("ConfiguracionHL7: " + nombreEstrategia);
        estrategia = cargarEstrategiaDesdeJar(jarDirectoryPath, nombreEstrategia + ".jar", "com.sideralsoft.estrategias." + nombreEstrategia);
        System.out.println("Estrategia ASIGNADA: " + nombreEstrategia );
        if (estrategia == null) {
            throw new IllegalArgumentException("No se encontró la estrategia: " + nombreEstrategia);
        }
        return estrategia;
    }

    private String identificarEstrategia(String mensaje) throws IOException {
        YamlReader yamlReader = YamlReader.getInstance(); //c.c.a()
        Map<String, Equipo> equipos = yamlReader.getEquipos(); //b()

        String[] lineas = mensaje.split("\\\\r?\\\\n|\\\\r");

        for (Equipo equipo : equipos.values()) {
            for (String campo : equipo.getCamposIdentificadores()) {
                String valorCampo = extraerCampoHL7(lineas, campo);
                if (valorCampo != null && valorCampo.equalsIgnoreCase(equipo.getNombre())) {
                    return equipo.getConfiguracionHl7();
                }
            }
        }
        return equipos.get("Default").getConfiguracionHl7();
    }

    private String extraerCampoHL7(String[] lineas, String campoHL7) {
        try {
            String[] partes = campoHL7.split("-");
            if (partes.length != 2) return null;

            String segmentoBuscado = partes[0];
            int indiceCampo = Integer.parseInt(partes[1]) - 1;

            for (String linea : lineas) {
                if (linea.startsWith(segmentoBuscado + "|")) {
                    String[] campos = linea.split("\\|");
                    if (indiceCampo < campos.length) {
                        return campos[indiceCampo];
                    }
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }


    private EstrategiaProcesamiento cargarEstrategiaDesdeJar(String jarDirectory, String jarName, String className) {
        try {
            // Verificar que el directorio existe
            File jarDir = new File(jarDirectory);
            if (!jarDir.exists() || !jarDir.isDirectory()) {
                throw new IllegalArgumentException("El directorio de JARs no existe: " + jarDirectory);
            }

            File[] jarFiles = jarDir.listFiles((dir, name) -> name.equalsIgnoreCase(jarName));
            if (jarFiles == null || jarFiles.length == 0) {
                throw new IllegalArgumentException("No se encontraron JARs en el directorio: " + jarDirectory);
            }

            URL[] urls = new URL[jarFiles.length];
            for (int i = 0; i < jarFiles.length; i++) {
                urls[i] = jarFiles[i].toURI().toURL();
                System.out.println("Cargando desde URL: " + urls[i]);
            }

            URLClassLoader classLoader = new URLClassLoader(urls, this.getClass().getClassLoader());

            Class<?> strategyClass = classLoader.loadClass(className);

            if (!EstrategiaProcesamiento.class.isAssignableFrom(strategyClass)) {
                throw new IllegalArgumentException("La clase " + className + " no implementa EstrategiaProcesamiento.");
            }

            return (EstrategiaProcesamiento) strategyClass.getDeclaredConstructor().newInstance();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al cargar la estrategia desde el JAR: " + className, e);
        }
    }
}
