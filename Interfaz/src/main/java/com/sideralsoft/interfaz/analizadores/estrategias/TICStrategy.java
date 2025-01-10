package com.sideralsoft.interfaz.analizadores.estrategias;

import com.sideralsoft.interfaz.analizadores.EnrutadorMensaje;
import com.sideralsoft.interfaz.comunicadores.ControladorHTTP;

public class TICStrategy implements EstrategiaProcesamiento {
    private EnrutadorMensaje enrutadorMensaje;
    private ControladorHTTP controladorHTTP;

    public TICStrategy(){
        this.enrutadorMensaje = new EnrutadorMensaje();
        this.controladorHTTP = new ControladorHTTP();
    }

    @Override
    public String procesarMensaje(String mensaje) {
        return "ACKTIC";
    }

    @Override
    public String analizarTipoMensaje(String mensaje) {
        String[] segments = mensaje.split("\r");
        for (String segment : segments) {
            if (segment.startsWith("MSH")) {
                String[] fields = segment.split("\\|");
                if (fields.length > 8) {
                    return fields[8];  // Tipo de mensaje, por ejemplo: "ORU^R01", "ACK", etc.
                }
            }
        }
        return null;
    }

    @Override
    public void validarMensaje(String clientAddress, String mensaje) {
        String status;
        String[] lines = mensaje.split("(?=MSH|PID|OBR|OBX)");

        // Verificar si contiene los segmentos obligatorios
        boolean hasMSH = false;
        boolean hasPID = false;
        boolean hasOBR = false;
        boolean hasOBX = false;

        for (String line : lines) {
            if (line.startsWith("MSH")) {
                hasMSH = true;
            } else if (line.startsWith("PID")) {
                hasPID = true;
            } else if (line.startsWith("OBR")) {
                hasOBR = true;
            } else if (line.startsWith("OBX")) {
                hasOBX = true;
            }
        }

        // Verificar que todos los segmentos obligatorios estén presentes
        if(hasMSH && hasPID && hasOBR && hasOBX){
            status = "AA";
            estructurarJSON(clientAddress, mensaje);
        }else {
            status = "AE";
        }
        mensaje = generarRespuestaConfirmacion(mensaje, status);
        enviarRespuestaConfirmacion(clientAddress, mensaje);

    }

    private void estructurarJSON(String clientAddress, String mensaje) {
        String json = "";
        controladorHTTP.enviarMensajeNube(json);
    }

    public void enviarRespuestaConfirmacion(String clientAddress, String mensaje) {
        enrutadorMensaje.enrutar(clientAddress, mensaje);
    }


    public String generarRespuestaConfirmacion(String mensaje, String status) {
        String[] segments = mensaje.split("\r");

        // Variables para extraer los datos necesarios del mensaje ORU
        String sendingApplication = "";
        String sendingFacility = "";
        String receivingApplication = "";
        String receivingFacility = "";
        String messageControlId = "";
        String processingId = "";
        String version = "";

        // Buscar el segmento MSH y extraer los datos
        for (String segment : segments) {
            if (segment.startsWith("MSH")) {
                String[] fields = segment.split("\\|");

                // Extraer valores del segmento MSH
                sendingApplication = fields[2];
                sendingFacility = fields[3];
                receivingApplication = fields[4];
                receivingFacility = fields[5];
                messageControlId = fields[9];
                processingId = fields[10];
                version = fields[11];
                break;
            }
        }

        // Fecha y hora fija para pruebas
        String fixedDateTime = "202412241300"; // Año 2024, diciembre 24, 13:00

        // Control ID fijo para el mensaje ACK
        String ackControlId = "ACK-54321";

        // Construir el mensaje ACK
        StringBuilder ackBuilder = new StringBuilder();
        ackBuilder.append("MSH|^~\\&|")
                .append(receivingApplication).append("|")
                .append(receivingFacility).append("|")
                .append(sendingApplication).append("|")
                .append(sendingFacility).append("|")
                .append(fixedDateTime).append("||ACK|")
                .append(ackControlId).append("|")
                .append(processingId).append("|")
                .append(version).append("|")
                .append("MSA|"+status+"|").append(messageControlId).append("|");
        return ackBuilder.toString();
    }

}
