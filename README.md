### 🟡 Prerequisitos
Para poder ejecutar este proyecto de forma correcta, se debe cumplir con los siguientes prerequisitos en tu máquina local:

- **IDE recomendado**: Intellij IDEA
- **JDK**: JDK versión 21 (configurado en la variable de entorno `JAVA_HOME`)
- **Maven**: Maven versión 3.9.9 (configurado en la variable de entorno `M2_HOME`)
> **Nota**: Asegúrate de que el JDK esté correctamente configurado en IntelliJ. Para hacerlo, ve a **File > Project Structure**. Ahí encontrarás dos configuraciones: **Project** y **SDKs**. Selecciona la versión del JDK que hayas configurado en `JAVA_HOME`.
>

---

## Interfaz de comunicación
### 🔵 Módulos
La interfaz de comunicación se compone de cuatro módulos:
- modulo-controladorConexiones
- modulo-distribuidorMensajes
- modulo-analizadorMensajes
- modulo-pruebas
> **Nota**: El módulo "modulo-pruebas" es una característica extra que no se encuentra en el diagrama de componentes. Dicho módulo sera el encargado de implementar todo tipo de prubas que aseguren y validen la calidad del producto.
>
### 🟢¿Cómo inicio?

Una vez clonado el repositorio abrimos el directorio `/Interfaz` en Intellij IDEA. La carpeta ya contiene todas las dependencias, por lo tanto, solo hace falta ejecutar la interfaz o el módulo de pruebas para su funcionamiento. Sin embargo, vamos a entender como ejecutarlos en caso de que haya actualizaciones en alguno de los módulos. 

Como se puede observar en el diagrama de componentes de la interfaz middleware, tanto el controlador de conexiones como el controlador de análisis de mensajes dependen del controlador de distribución de mensajes. Por lo tanto, si se realiza algún cambio en el `modulo-distribuidorMensajes` tendremos que ejecutar el siguiente comando encargado de limpiar y contruir el módulo especificado:
```bash
mvn clean install -pl modulo-distribuidorMensajes
```

Este comando habrá generado una carpeta `/target` que contiene el jar respectivo `modulo-distribuidorMensajes-1.0-SNAPSHOT.jar` 

Finalmente, para que los demas módulos cuenten con la dependencia actualizada del `modulo-distribuidorMensajes` recargamos los demas módulos: `modulo-analizadorMensajes` y `modulo-controladorConexiones`.

> **Nota**: Como el `modulo-analizadorMensajes` y `modulo-controladorConexiones` son independeintes de si mismos, no importa el orden en que los recarges.
>

---

### ⏺️¿Cómo genero el jar del controlador de conexiones?

Para generar el jar del `modulo-controladorConexiones` usamos el mismo comando para limpiar y construir el módulo especificado:
```bash
mvn clean install -pl modulo-controladorConexiones
```
Este comando habrá generado una carpeta `/target` que contiene el jar respectivo `modulo-controladorConexiones.jar`.

### 🔴¿Cómo agrego estrategias de interpretación?

Para agregar estrategias de interpretación debemos generar el jar respectivo con `mvn clean install -pl modulo-analizadorMensajes -Djar.name={NombreJar} -Dclass.to.include={NombreEstrategia}` para posteriormente colocarlo en `C:\instalaciones\interfaz-hl7\lib`. 

Este comando habrá generado una carpeta `/target` que contiene la dependencia esperada con su versión ofuscada. Por ejemplo:
- `AnalyzerStrategy.jar`
- `AnalyzerStrategy-ofuscated.jar` (versión ofuscada)

Si agregamos una nueva dependencia debemos registrarlo en el archivo de configuraciones `equipos.yaml`  en `C:\config`. Por ejemplo:

```bash
equipos:
	AnalyzerA:
		id: "A1"
		campos_identificadores: ["MSH-3", "MSH-5", "OBX-18"]
		configuracion_hl7: AnalyzerStrategy
		tipoConexion: "cliente"
		ip: "localhost"
		puerto: 3001
		token: "token"
```

Donde la clave `configuracion_hl7` corresponde a la nueva dependencia agregada y `campos_identificadores` a los campos donde suele venir especificado el nombre del equipo de laboratorio que envía el mensaje. 

> **Nota**: Las dos versiones geenradas pueden ser utilizadas en `C:\instalaciones\interfaz-hl7\lib`, solo asegurarse que el nombre de la dependencia corresponda al especificado en el archivo de configuraciones `equipos.yaml`  en `C:\config`.
>

### 🟠 ¿Cómo estructuro mi archivo de configuraciones?

El archivo de configuraciones tiene la siguiente estructura:
```bash
equipos:
	NombreEquipoLaboratorio1:
		id: "id"
		campos_identificadores: ["MSH-X", "MSH-Y", "OBX-X"]
		configuracion_hl7: Dependencia
		tipoConexion: "tipoConexion"
		ip: "ip"
		puerto: puerto
		token: "token"

	NombreEquipoLaboratorio2:
		id: "id"
		campos_identificadores: ["MSH-X", "MSH-Y", "OBX-X"]
		configuracion_hl7: Dependencia
		tipoConexion: "tipoConexion"
		ip: "ip"
		puerto: puerto
		token: "token"

	...
	
	NombreEquipoLaboratorioN:...
```

Donde:
- id: identificador del equipo de laboratorio.
- campos_identificadores: campos donde suele venir especificado el nombre del equipo de laboratorio que envía el mensaje.
- configuracion_hl7: nombre de la dependencia encargada de interpretar mensajes HL7.
- tipoConexion: tipo de conexión del equipo de laboratorio (cliente o servidor).
- ip: dirección ip del equipo de laboratorio en caso de el equipo de laboratorio actúe como "servidor", de otro modo esta sección puede ir vacía.
- puerto: puerto del equipo de laboratorio en caso de que actúe como "servidor", sino, puerto del dispositivo que ejecuta la interfaz cuando el equipo de laboratorio actúe como "cliente".
- token: token correspondiente al identificador (id) del equipo de laboratorio.

---
## Emulador

### 🟢¿Cómo inicio?

Una vez clonado el repositorio abrimos una terminal en `Orion/Emulador`. Dentro, solamente ejecutamos el siguiente comando para levantar el servicio: `php artisan serve`.

---

### 🟣¿Cómo genero tokens de seguridad?

Una vez clonado el repositorio abrimos una terminal en `Orion/Emulador`. Dentro, solamente ejecutamos el siguiente comando para generar el token firmado con el identificador del equipo de laboratorio: `php artisan generate:token {id}`.

---

### 🔴¿Cómo agrego estrategias de interpretación?

Al igual que la interfaz, el emulador tambíen adopta el patrón de diseño Strategy. Por lo tanto, si queremos agregar nuevas estrategias de interpretación, debemos respetar dicho diseño. 

---

## Pruebas

### ⭕¿Cómo pruebo la interfaz de comunicación?

Para probar la interfaz de comunicación ocuparemos el módulo de pruebas `modulo-pruebas` . En dicho módulo ejecutamos la siguiente clase `CucumberTestRunner.java` en `src/test/java/com/sideralsoft/test/runners`.

Una vez que s ehayan ejecutado las pruebas, se verá reflejado el reporte en `target/cucumber-reports.html`.
> **Nota**: Asegurarse de haber recargado el módulo de pruebas en caso de actualizar los módulos: `modulo-controladorConexiones` y  `modulo-distribuidorMensajes` .
>
> **Nota**: Para la ejecución exitosa de las pruebas es necesario levantar el emulador.
>
---

### ⭕¿Cómo pruebo el emulador?

Para probar el emulador podemos hacer las siguientes dos request:
1. Envío de resultados clínicos (ORU):
- Request a `http://localhost:8000/api/resultados`:
```json
{
	"id":"<ID>",
	"token":"token",
	"estrategiaHL7":"<ESTRATEGIA_HL7>",
	"hl7Trama":"hl7Trama"
}
```

- Response:
```json
{
	"success":"<true||false>",
	"data" : "<resultadosClínicosJSON>"
}
```

2. Consulta de órdenes pendientes (QRY):
- Request a `http://localhost:8000/api/ordenes`:
```json
{
	"id":"<ID>",
	"token":"token",
	"estrategiaHL7":"<ESTRATEGIA_HL7>",
	"hl7Trama":"hl7Trama"
}
```

- Response:
```json
{
	"success":"<true||false>",
	"orden" : "<ordenesPendientesHL7>"
}
```

---
