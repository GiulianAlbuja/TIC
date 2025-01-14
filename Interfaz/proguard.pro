# Archivos JAR de entrada y salida
-injars out/artifacts/Interfaz_jar/Interfaz.jar
-outjars out/artifacts/Interfaz_jar/Interfaz-ofuscado.jar

# Mantener la clase principal y evitar renombramiento
-keep public class com.sideralsoft.interfaz.SwingServerUI {
    public static void main(java.lang.String[]);
}

# Excluir archivos específicos


# Excluir clases específicas del análisis
-keep class com.sideralsoft.excluded.** { *; }

# Eliminar una clase específica (si quieres que no se empaquete en el JAR)
-keep class com.sideralsoft.interfaz.analizadores.estrategias.SlaytherStrategy

# Evitar que ProGuard renombre ciertos paquetes completos
#-keep class com.sideralsoft.comunicadores.** { *; }

# Evitar renombrar clases de librerías importantes (como Gson, JavaFX, etc.)
-keep class com.google.gson.** { *; }
-keep class javafx.** { *; }
