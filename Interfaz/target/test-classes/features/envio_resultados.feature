#language: es
Característica: Envío de resultados clínicos desde el equipo de laboratorio a Orión

  Escenario: El equipo de laboratorio actúa como cliente y envía resultados clínicos a Orion
    #Dado que el equipo de laboratorio actúa como cliente
    Dado que la interfaz de comunicación ha iniciado una sesión de tipo "cliente" con el equipo de laboratorio
    Cuando la interfaz de comunicación reciba un mensaje ORU
    """
    MSH|^~\&|LabSystem|LabFacility|LIS|Hospital|202412241200||ORU^R01|98765|P|2.3|\rPID|1||123456^^^Hospital^MR||Doe^Jane||19900101|F|||456 Elm St^^Metropolis^NY^12345||555-555-1234|\rOBR|1|54321|98765|BMP^Basic Metabolic Panel^L|||202412241100|202412241200|\rOBX|1|NM|2951-2^Sodium^LN||140|mmol/L|135-145|N|||\rOBX|2|NM|2823-3^Potassium^LN||4.2|mmol/L|3.5-5.0|N|||\r
    """
    Entonces la interfaz de comunicación envía al equipo de laboratorio una respuesta de confirmación ACK
    """
    MSH|^~\&|LIS|Hospital|LabSystem|LabFacility|202412241300||ACK|ACK-54321|P|2.3|MSA|AA|98765|
    """
    Y envía los resultados clínicos a Orion
    """
    {"ip":"/127.0.0.1","id":"1","token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3MzY3NTIyNzcsImV4cCI6MTczNjc1NDA3NywiY2xpZW50SWQiOiIxIn0.-g863Yibb1z8LKiwRhvFnfdKYeWoVT0UYBMh3lt8f68","codigoEquipo":"TC-220","hl7Trama":"MSH|^~\\&|LabSystem|LabFacility|LIS|Hospital|202412241200||ORU^R01|98765|P|2.3|\\rPID|1||123456^^^Hospital^MR||Doe^Jane||19900101|F|||456 Elm St^^Metropolis^NY^12345||555-555-1234|\\rOBR|1|54321|98765|BMP^Basic Metabolic Panel^L|||202412241100|202412241200|\\rOBX|1|NM|2951-2^Sodium^LN||140|mmol/L|135-145|N|||\\rOBX|2|NM|2823-3^Potassium^LN||4.2|mmol/L|3.5-5.0|N|||\\r"}
    """


  #Escenario: El equipo de laboratorio actúa como servidor y envía resultados clínicos a Orion
  #  Dado que el equipo de laboratorio actúa como servidor
  #  Y que la interfaz de comunicación ha iniciado una sesión de tipo "servidor" con el equipo de laboratorio
  #  Cuando la interfaz de comunicación reciba un mensaje ORU
  #  """
  #  MSH|^~\&|LabSystem|LabFacility|LIS|Hospital|202412241200||ORU^R01|98765|P|2.3|\rPID|1||123456^^^Hospital^MR||Doe^Jane||19900101|F|||456 Elm St^^Metropolis^NY^12345||555-555-1234|\rOBR|1|54321|98765|BMP^Basic Metabolic Panel^L|||202412241100|202412241200|\rOBX|1|NM|2951-2^Sodium^LN||140|mmol/L|135-145|N|||\rOBX|2|NM|2823-3^Potassium^LN||4.2|mmol/L|3.5-5.0|N|||\r
  #  """
  #  Entonces la interfaz de comunicación envía al equipo de laboratorio una respuesta de confirmación ACK
  #  """
  #  MSH|^~\&|LIS|Hospital|LabSystem|LabFacility|202412241300||ACK|ACK-54321|P|2.3|MSA|AA|98765|
  #  """
  #  Y envía los resultados clínicos a Orion
  #  """
  #  {"ip":"/127.0.0.1","id":"1","token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3MzY3NTIyNzcsImV4cCI6MTczNjc1NDA3NywiY2xpZW50SWQiOiIxIn0.-g863Yibb1z8LKiwRhvFnfdKYeWoVT0UYBMh3lt8f68","codigoEquipo":"TC-220","hl7Trama":"MSH|^~\\&|LabSystem|LabFacility|LIS|Hospital|202412241200||ORU^R01|98765|P|2.3|\\rPID|1||123456^^^Hospital^MR||Doe^Jane||19900101|F|||456 Elm St^^Metropolis^NY^12345||555-555-1234|\\rOBR|1|54321|98765|BMP^Basic Metabolic Panel^L|||202412241100|202412241200|\\rOBX|1|NM|2951-2^Sodium^LN||140|mmol/L|135-145|N|||\\rOBX|2|NM|2823-3^Potassium^LN||4.2|mmol/L|3.5-5.0|N|||\\r"}
  #  """
