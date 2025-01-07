#language: es
Característica: Envío de resultados clínicos desde el equipo de laboratorio a Orión

  Escenario: El equipo de laboratorio envía resultado clínicos a la interfaz de comunicación
    Dado que la interfaz de comunicación ha iniciado una sesión con el equipo de laboratorio
    Cuando la interfaz de comunicación reciba un mensaje ORU
    """
    MSH|^~\&|LabSystem|LabFacility|LIS|Hospital|202412241200||ORU^R01|98765|P|2.3|PID|1||123456^^^Hospital^MR||Doe^Jane||19900101|F|||456 Elm St^^Metropolis^NY^12345||555-555-1234|OBR|1|54321|98765|BMP^Basic Metabolic Panel^L|||202412241100|202412241200|||||||Dr. Jones|||||202412241300||Lab^Facility|OBX|1|NM|2951-2^Sodium^LN||140|mmol/L|135-145|N|||F|OBX|2|NM|2823-3^Potassium^LN||4.2|mmol/L|3.5-5.0|N|||F|
    """
    Entonces la interfaz de comunicación procesa el mensaje "ORU^R01"
    Y genera una respuesta de confirmación "ACK^R01"
    """
    MSH|^~\&|LIS|Hospital|LabSystem|LabFacility|202412241300||ACK|54321|P|2.3|
    MSA|AA|98765
    """
    Y el estado de envío de resultados clínicos está en "pediente"


  Escenario: La interfaz de comunicación envía los resultados clínicos a Orión
  #  Dado que el estado de envío de resultado clínicos está en "pendiente"
  #  Cuando la interfaz de comunicación estructure un mensaje JSON con los resutados clínicos
  #  """
  #  "equipo" : "Tipo3",
  #  "tramaHL7" : {
  #    "PID" : "|1||123456^^^Hospital^MR||Doe^Jane||19900101|F|||456 Elm St^^Metropolis^NY^12345||555-555-1234|"
  #    "OBR" : "|1|54321|98765|BMP^Basic Metabolic Panel^L|||202412241100|202412241200|||||||Dr. Jones|||||202412241300||Lab^Facility|"
  #    "OBX" : "|1|NM|2951-2^Sodium^LN||140|mmol/L|135-145|N|||F|"
  #    "OBX" : "|2|NM|2823-3^Potassium^LN||4.2|mmol/L|3.5-5.0|N|||F|"
  #  }
  #  """
  #  Entonces la interfaz de comunicación envía el mensaje JSON a Orión
