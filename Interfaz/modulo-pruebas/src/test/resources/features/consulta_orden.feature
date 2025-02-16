#language: es
Característica: Consulta de órdenes pendientes desde el equipo de laboratorio a Orión

Esquema del escenario: El equipo de laboratorio consulta órdenes pendientes a Orión
   Dado que el equipo de laboratorio actúa como <TIPO_CONEXION>
   Cuando la interfaz de comunicación reciba un mensaje de consulta QRY
   """
   MSH|^~\&|<QRY-MSH-3>|LabFacility|LIS|Hospital|202402061200||QRY|123456|P|2.3|\rQRD|202402061200|R|I|123456|<QRY-QRD-6>|<QRY-QRD-7>|RES|\r
   """
   Entonces la interfaz de comunicación envía al equipo de laboratorio una respuesta de confirmación QCK
   """
   MSH|^~\&|LIS|Hospital|<QCK-MSH-5>|LabFacility|202402061201||QCK|QCK-654321|P|2.3|MSA|AA|123456|
   """
   Y envía la consulta de órdenes pendientes a Orión
   """
   {"id":"<ID>","token":"token","estrategiaHL7":"<ESTRATEGIA_HL7>","hl7Trama":"hl7Trama"}
   """
   Y envía la información de muestra al equipo de laboratorio
   """
   MSH|^~\&|LIS|Hospital|<QCK-MSH-5>|LabFacility|202402061202||DSR|654321|P|2.3|\rPID|1|12345^^^Hospital^MR||Doe^John||19800101|M|||456 Elm St^^Metropolis^NY^12345||555-555-5555|\rOBR|1|54321|12345|CMP^Comprehensive Metabolic Panel^L|||202402061100|202402061201\r
   """
   Ejemplos:
   | TIPO_CONEXION | QRY-MSH-3     | QRY-QRD-6     | QRY-QRD-7    | QCK-MSH-5     | ID | ESTRATEGIA_HL7      |
   | servidor      | Default      | Ejemplo       | Ejemplo      | Default      | D  | DefaultStrategy     |