#language: es
Característica: Envío de resultados clínicos desde el equipo de laboratorio a Orión

  Esquema del escenario: El equipo de laboratorio actúa como cliente y envía resultados clínicos a Orion
    Dado que el equipo de laboratorio actúa como <TIPO_EQUIPO>
    Y que la interfaz de comunicación ha iniciado una sesión con el equipo de laboratorio <TIPO_EQUIPO>
    Cuando la interfaz de comunicación reciba un mensaje ORU
    """
    MSH|^~\&|<ORU-MSH-3>|LabFacility|LIS|Hospital|202412241200||ORU^R01|98765|P|2.3|\rPID|1|<ORU-PID-3>|123456^^^Hospital^MR||Doe^Jane||19900101|F|||456 Elm St^^Metropolis^NY^12345||555-555-1234|\rOBR|1|54321|<ORU-OBR-4>|BMP^Basic Metabolic Panel^L|||202412241100|202412241200|\rOBX|1|NM|2951-2^Sodium^LN||140|mmol/L|135-145|N|||\rOBX|2|NM|2823-3^Potassium^LN||4.2|mmol/L|3.5-5.0|N|||\r
    """
    Entonces la interfaz de comunicación envía al equipo de laboratorio una respuesta de confirmación ACK
    """
    MSH|^~\&|<ACK-MSH-3>|<ACK-MSH-4>|<ACK-MSH-5>|LabFacility|202412241300||ACK|ACK-54321|P|2.3|MSA|AA|98765|
    """
    Y envía los resultados clínicos a Orion
    """
    {"ip":"<IP>","id":"<ID>","token":"<TOKEN>","configuracionHL7":"<CONFIGURACION_HL7>","hl7Trama":"MSH|^~\\&|<ORU-MSH-3>|LabFacility|LIS|Hospital|202412241200||ORU^R01|98765|P|2.3|\\rPID|1|<ORU-PID-3>|123456^^^Hospital^MR||Doe^Jane||19900101|F|||456 Elm St^^Metropolis^NY^12345||555-555-1234|\\rOBR|1|54321|<ORU-OBR-4>|BMP^Basic Metabolic Panel^L|||202412241100|202412241200|\\rOBX|1|NM|2951-2^Sodium^LN||140|mmol/L|135-145|N|||\\rOBX|2|NM|2823-3^Potassium^LN||4.2|mmol/L|3.5-5.0|N|||\\r"}
    """
    Ejemplos:
      | CONFIGURACION_HL7  | TIPO_EQUIPO | ORU-MSH-3 | ORU-PID-3     | ORU-OBR-4 | ACK-MSH-3 | ACK-MSH-4 | ACK-MSH-5 | IP         | ID | TOKEN                                                                                                                                                 |
      | AnalyzerStrategy   | cliente     | AnalyzerA | Ejemplo       | Ejemplo   | LIS       | Hospital  | AnalyzerA | /127.0.0.1 | A1 | eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3Mzg3ODA1OTYsImV4cCI6MTczODgwOTM5NiwiY2xpZW50SWQiOiJBMSJ9.gqSVrz2SkykBkHbnmw0aNGObW9t0ijUAd9cML5vTDm4 |
      | HematologyStrategy | servidor    | Ejemplo   | HematologyPro | Ejemplo   | LIS       | Hospital  | Ejemplo   | /127.0.0.2 | H1 | eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3Mzg3ODA2MjQsImV4cCI6MTczODgwOTQyNCwiY2xpZW50SWQiOiJIMSJ9.GI6aaO0MDDWeCOAZyStEB6YpOGuHogZFVrVYU3Y5Xv4 |
      | AnalyzerStrategy   | cliente     | AnalyzerA | Ejemplo       | Ejemplo   | LIS       | Hospital  | AnalyzerA | /127.0.0.1 | A1 | eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3Mzg3ODA1OTYsImV4cCI6MTczODgwOTM5NiwiY2xpZW50SWQiOiJBMSJ9.gqSVrz2SkykBkHbnmw0aNGObW9t0ijUAd9cML5vTDm4 |
      | HematologyStrategy | servidor    | Ejemplo   | HematologyPro | Ejemplo   | LIS       | Hospital  | Ejemplo   | /127.0.0.2 | H1 | eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3Mzg3ODA2MjQsImV4cCI6MTczODgwOTQyNCwiY2xpZW50SWQiOiJIMSJ9.GI6aaO0MDDWeCOAZyStEB6YpOGuHogZFVrVYU3Y5Xv4 |



#CORREGIR
      #| BioAnalyzerStrategy | cliente     | Ejemplo   | Ejemplo            | BioAnalyzerX | LIS       | Hospital  | BioAnalyzerX       | /127.0.0.3 | B1 | eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3Mzg3ODA2NDIsImV4cCI6MTczODgwOTQ0MiwiY2xpZW50SWQiOiJCMSJ9.Y_cHHxZTsbODy9ZziIdm-1v64d_ALanHvCUCY8ZiHzQ |
      #| DefaultStrategy     | servidor    | Ejemplo   | EquipoNoRegistrado | Ejemplo      | LIS       | Hospital  | EquipoNoRegistrado | /127.0.0.4 | D  | eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3Mzg3ODA2NjEsImV4cCI6MTczODgwOTQ2MSwiY2xpZW50SWQiOiJEIn0.kv3fH-36k8QFh8qUKa0doXiqmZWNuo7uBraZj3QmajQ  |