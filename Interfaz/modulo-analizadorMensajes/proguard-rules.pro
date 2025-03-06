# Mantener nombres de la interfaz y métodos públicos de la interfaz EstrategiaProcesamiento
-keep interface com.sideralsoft.shared.estrategias.EstrategiaProcesamiento

# Mantener los métodos de la interfaz en las clases que la implementan
-keep class * implements com.sideralsoft.shared.estrategias.EstrategiaProcesamiento {
    public *;
}

# Mantener las clases de la estructura básica de mensajes
-keep class com.sideralsoft.shared.entidades.** { *; }
-keep class com.sideralsoft.shared.comunicadores.** { *; }
-keep class com.sideralsoft.shared.readers.** { *; }

# Evitar renombrar clases importantes para la ejecución
-keepattributes *Annotation*
-keepattributes Signature

# Usa el JDK 21 en lugar de rt.jar (Java 8)
-libraryjars C:/Users/darmy/.jdks/corretto-21.0.4/jmods/java.base.jmod

# Minimizar el código eliminando clases no utilizadas
-dontshrink



