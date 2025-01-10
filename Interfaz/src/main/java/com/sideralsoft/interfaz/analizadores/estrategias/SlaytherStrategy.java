package com.sideralsoft.interfaz.analizadores.estrategias;

import com.sideralsoft.interfaz.analizadores.EnrutadorMensaje;

public class SlaytherStrategy implements  EstrategiaProcesamiento{
    private EnrutadorMensaje enrutadorMensaje;

    public SlaytherStrategy(){
        this.enrutadorMensaje = new EnrutadorMensaje();
    }

    @Override
    public String procesarMensaje(String mensaje) {
        return "ACK-SLAYTHER";
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
        System.out.println("VALIDAR-SLAYTHER");
    }

    public void enviarRespuestaConfirmacion(String clientAddress, String mensaje) {
        enrutadorMensaje.enrutar(clientAddress, generarRespuestaConfirmacion(mensaje));
    }

    public String generarRespuestaConfirmacion(String mensaje) {
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
                .append("MSA|AA|").append(messageControlId).append("|");
        return ackBuilder.toString();
    }
}
