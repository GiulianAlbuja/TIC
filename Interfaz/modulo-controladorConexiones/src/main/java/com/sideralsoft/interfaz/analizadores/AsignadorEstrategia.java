package com.sideralsoft.interfaz.analizadores;

import com.sideralsoft.shared.entidades.Equipo;
import com.sideralsoft.shared.estrategias.EstrategiaProcesamiento;
import com.sideralsoft.shared.readers.JsonReader;

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
          String jarDirectoryPath = "C:\\instalaciones\\interfaz-hl7\\lib";
          switch (codigoEquipo) {
              case "TIC":
                  estrategia = cargarEstrategiaDesdeJar(jarDirectoryPath, "TICStrategy.jar" ,"com.sideralsoft.estrategias.TICStrategy");
                  System.out.println("Estrategia ASIGNADA TIC- actualizacion");
                  return estrategia;
              case "TC-220":
                  estrategia = cargarEstrategiaDesdeJar(jarDirectoryPath, "TICStrategy.jar" ,"com.sideralsoft.estrategias.TICStrategy");
                  System.out.println("Estrategia ASIGNADA SLAYTHER");
                  return estrategia;
              default:
                  System.out.println("No se encontró estrategia para el equipo con código: " + codigoEquipo);
                  return null;
          }
      }

   private EstrategiaProcesamiento cargarEstrategiaDesdeJar(String jarDirectory, String jarName,String className) {
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
