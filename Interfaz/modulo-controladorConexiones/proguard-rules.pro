# Evitar que ProGuard elimine clases del JDK necesarias
-libraryjars <java.home>/lib/modules
-libraryjars C:/Users/darmy/.jdks/corretto-21.0.4/jmods/java.base.jmod

# Deshabilitar la ofuscación para probar (elimina esto si quieres permitir la ofuscación después)
-dontoptimize
-dontshrink
-dontpreverify
-dontnote
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontwarn com.sideralsoft.**
-dontwarn com.fasterxml.jackson.**
-dontwarn org.yaml.snakeyaml.**
-dontwarn com.sun.**
-dontwarn javafx.**
-dontwarn com.google.**

# No ofuscar la clase YamlReader
-keep class com.sideralsoft.shared.readers.YamlReader { *; }

# No modificar los miembros (variables y métodos) de YamlReader
-keepclassmembers class com.sideralsoft.shared.readers.YamlReader {
    *;
}

# Ofuscar otras clases de la aplicación (si así lo deseas)
-keep class com.sideralsoft.interfaz.analizadores.** { *; }
