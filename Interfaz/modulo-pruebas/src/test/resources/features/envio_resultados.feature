#language: es
Característica: Envío de resultados clínicos desde el equipo de laboratorio a Orión

  Esquema del escenario: El equipo de laboratorio envía resultados clínicos a Orión
    Dado que el equipo de laboratorio actúa como <TIPO_CONEXION>
    Cuando la interfaz de comunicación reciba un mensaje ORU
    """
    MSH|^~\&|<ORU-MSH-3>|LabFacility|LIS|Hospital|202412241200||ORU^R01|98765|P|2.3|\rPID|1|<ORU-PID-3>|123456^^^Hospital^MR||Doe^Jane||19900101|F|||456 Elm St^^Metropolis^NY^12345||555-555-1234|\rOBR|1|54321|<ORU-OBR-4>|BMP^Basic Metabolic Panel^L|||202412241100|202412241200|\rOBX|1|NM|2951-2^Sodium^LN||140|mmol/L|135-145|N|||\rOBX|2|NM|2823-3^Potassium^LN||4.2|mmol/L|3.5-5.0|N|||\r
    """
    Entonces la interfaz de comunicación envía al equipo de laboratorio una respuesta de confirmación ACK
    """
    MSH|^~\&|LIS|Hospital|<ACK-MSH-5>|LabFacility|202412241300||ACK|ACK-54321|P|2.3|MSA|AA|98765|
    """
    Y envía los resultados clínicos a Orión
    """
    {"id":"<ID>","token":"token","estrategiaHL7":"<ESTRATEGIA_HL7>","hl7Trama":"hl7Trama"}
    """
    Ejemplos:
      | TIPO_CONEXION | ORU-MSH-3 | ORU-PID-3     | ORU-OBR-4    | ACK-MSH-5     | ID | ESTRATEGIA_HL7      |
      | cliente       | AnalyzerA | Ejemplo       | Ejemplo      | AnalyzerA     | A1 | AnalyzerStrategy    |
      | servidor      | Ejemplo   | HematologyPro | Ejemplo      | HematologyPro | H1 | HematologyStrategy  |
      | cliente       | Ejemplo   | Ejemplo       | BioAnalyzerX | BioAnalyzerX  | B1 | BioAnalyzerStrategy |
      | servidor      | Default   | Ejemplo       | Ejemplo      | Default       | D  | DefaultStrategy     |