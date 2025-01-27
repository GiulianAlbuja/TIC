package com.sideralsoft.interfaz.analizadores;

import com.sideralsoft.interfaz.Entidades.Equipo;
import com.sideralsoft.interfaz.analizadores.estrategias.EstrategiaProcesamiento;
import com.sideralsoft.interfaz.analizadores.estrategias.SlaytherStrategy;
import com.sideralsoft.interfaz.analizadores.estrategias.TICStrategy;
import com.sideralsoft.interfaz.readers.JsonReader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

public class AsignadorEstrategia {
    private EstrategiaProcesamiento estrategia;

    public EstrategiaProcesamiento asignarEstrategia(String clientAddress) throws IOException {
        JsonReader jsonReader = JsonReader.getInstance();
        Equipo equipo = jsonReader.getEquipoByIp(clientAddress);
        String codigoEquipo = equipo.getCodigoEquipo();
        switch (codigoEquipo){
            case "TIC":
                //Metodo libreria para cargar la estrategia
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

  // public EstrategiaProcesamiento asignarEstrategia(String clientAddress) throws IOException {
  //     JsonReader jsonReader = JsonReader.getInstance();
  //     Equipo equipo = jsonReader.getEquipoByIp(clientAddress);
  //     String codigoEquipo = equipo.getCodigoEquipo();

  //     String jarDirectoryPath = "C:\\estrategias";

  //     switch (codigoEquipo) {
  //         case "TIC":
  //             estrategia = cargarEstrategiaDesdeJar(jarDirectoryPath, "TICStrategy");
  //             System.out.println("Estrategia ASIGNADA TIC");
  //             return estrategia;

  //         case "TC-220":
  //             estrategia = cargarEstrategiaDesdeJar(jarDirectoryPath, "SlaytherStrategy");
  //             System.out.println("Estrategia ASIGNADA SLAYTHER");
  //             return estrategia;

  //         default:
  //             System.out.println("No se encontró estrategia para el equipo con código: " + codigoEquipo);
  //             return null;
  //     }
  // }


   //private EstrategiaProcesamiento cargarEstrategiaDesdeJar(String jarDirectory, String className) {
   //    try {
   //        // Verificar que el directorio existe
   //        File jarDir = new File(jarDirectory);
   //        if (!jarDir.exists() || !jarDir.isDirectory()) {
   //            throw new IllegalArgumentException("El directorio de JARs no existe: " + jarDirectory);
   //        }

   //        // Crear un ClassLoader para cargar los JARs
   //        File[] jarFiles = jarDir.listFiles((dir, name) -> name.endsWith(".jar"));
   //        if (jarFiles == null || jarFiles.length == 0) {
   //            throw new IllegalArgumentException("No se encontraron JARs en el directorio: " + jarDirectory);
   //        }

   //        // Convertir los JARs a URLs
   //        URL[] urls = new URL[jarFiles.length];
   //        for (int i = 0; i < jarFiles.length; i++) {
   //            urls[i] = jarFiles[i].toURI().toURL();
   //            System.out.println("Cargando desde URL: " + urls[i]);
   //        }

   //        // Crear un ClassLoader para los JARs
   //        URLClassLoader classLoader = new URLClassLoader(urls, this.getClass().getClassLoader());

   //        // Cargar la clase sin paquete (asumiendo que className es el nombre simple de la clase, como "TICStrategy")
   //        Class<?> strategyClass = classLoader.loadClass(className);

   //        // Verificar que la clase implementa EstrategiaProcesamiento
   //        if (!EstrategiaProcesamiento.class.isAssignableFrom(strategyClass)) {
   //            throw new IllegalArgumentException("La clase " + className + " no implementa EstrategiaProcesamiento.");
   //        }

   //        // Crear una nueva instancia de la estrategia
   //        return (EstrategiaProcesamiento) strategyClass.getDeclaredConstructor().newInstance();

   //    } catch (Exception e) {
   //        e.printStackTrace();
   //        throw new RuntimeException("Error al cargar la estrategia desde el JAR: " + className, e);
   //    }
   //}



}
