# ============================
#  🔒 CONFIGURACIÓN PROGUARD
# ============================

# ==============
# 📌 JAVA & JDK
# ==============

# Evitar que ProGuard elimine clases del JDK necesarias
-libraryjars <java.home>/lib/modules
-libraryjars C:/Users/darmy/.jdks/corretto-21.0.4/jmods/java.base.jmod

# =======================
# 🔷 JAVA FX Y DEPENDENCIAS
# =======================

# Mantener todas las clases de JavaFX para evitar problemas de reflexión
-keep class javafx.** { *; }
-keep class com.sun.javafx.** { *; }
-keep class com.sun.glass.** { *; }
-keep class com.sun.scenario.** { *; }
-keep class com.sun.prism.** { *; }

# Evitar optimización y eliminación de clases JavaFX
-dontshrink
-dontoptimize

# Evitar eliminación de clases internas y métodos que JavaFX usa por reflexión
-keepclassmembers class javafx.** {
    public <init>(...);
    public *;
}

# Mantener clases necesarias para carga de archivos FXML
-keep class javafx.fxml.FXMLLoader { *; }
-keep class javafx.scene.** { *; }

# Mantener clases necesarias para gráficos y animaciones en JavaFX
-keep class javafx.animation.** { *; }
-keep class javafx.scene.effect.** { *; }
-keep class javafx.stage.** { *; }
-keep class javafx.scene.image.** { *; }
-keep class javafx.scene.paint.** { *; }
-keep class javafx.scene.canvas.** { *; }

# =======================
# 🏗️  ESTRUCTURA DEL PROYECTO
# =======================

# Mantener clases y estructura del paquete UI (interfaz gráfica)
-keep class com.sideralsoft.interfaz.componentesUI.** { *; }

# Mantener la clase principal y su método main
-keep class com.sideralsoft.interfaz.MainApp {
    public static void main(java.lang.String[]);
}

# Mantener clases usadas en reflexión o inyección
-keep class com.sideralsoft.shared.estrategias.EstrategiaProcesamiento
-keep class * implements com.sideralsoft.shared.estrategias.EstrategiaProcesamiento {
    public *;
}

# Mantener clases de entidades, comunicadores y readers
-keep class com.sideralsoft.shared.entidades.** { *; }
-keep class com.sideralsoft.shared.comunicadores.** { *; }
-keep class com.sideralsoft.shared.readers.** { *; }

# Mantener clases de analizadores
-keep class com.sideralsoft.interfaz.analizadores.** { *; }

# =======================
# 📝 YAML (SnakeYAML)
# =======================

# Mantener todas las clases de SnakeYAML para evitar problemas con reflexión
-keep class org.yaml.snakeyaml.** { *; }

# Mantener métodos de SnakeYAML utilizados dinámicamente
-keepclassmembers class org.yaml.snakeyaml.** {
    *;
}

# =======================
# 📦 JACKSON (Serialización JSON)
# =======================

# Mantener clases necesarias para Jackson (evita errores de serialización)
-keep class com.fasterxml.jackson.** { *; }

# Evitar eliminación de clases utilizadas en la serialización de objetos JSON
-keepclassmembers class * {
    @com.fasterxml.jackson.annotation.JsonCreator <init>(...);
    @com.fasterxml.jackson.annotation.JsonProperty *;
}

# =======================
# ⚙️ OPTIMIZACIONES Y AJUSTES
# =======================

# Mantener interfaces y métodos públicos
-keep interface * { *; }

# Evitar cambios en nombres de métodos para prevenir conflictos en dependencias externas
-adaptclassstrings

# Mantener métodos públicos y constructores en todas las clases
-keepclassmembers class * {
    public <init>(...);
    public void *(...);
}

# =======================
# 🚫 PREVENCIÓN DE ADVERTENCIAS
# =======================

# Evitar advertencias de clases faltantes si no afectan la ejecución
-dontwarn com.sideralsoft.**
-dontwarn javax.**
-dontwarn com.fasterxml.jackson.**
-dontwarn org.yaml.snakeyaml.**
-dontwarn com.sun.**
-dontwarn javafx.**

# =======================
# 🚀 AJUSTES DE PROGUARD
# =======================

# Evitar eliminación de clases utilizadas en la serialización
-keepnames class org.yaml.snakeyaml.**

# Evitar eliminación de métodos de clases serializables
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    private void readObjectNoData();
}

# Mantener atributos críticos de reflexión y anotaciones
-keepattributes Exceptions, InnerClasses, Signature, Deprecated, SourceFile, LineNumberTable, *Annotation*

# =======================
# ⏳ EVITAR ERRORES DE RUNTIME
# =======================

# Deshabilitar optimización y eliminación de código para evitar problemas de compatibilidad con JavaFX
-dontshrink
-dontoptimize
