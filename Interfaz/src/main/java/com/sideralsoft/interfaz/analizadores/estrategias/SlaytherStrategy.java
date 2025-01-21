package com.sideralsoft.interfaz.analizadores.estrategias;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sideralsoft.interfaz.Entidades.Equipo;
import com.sideralsoft.interfaz.analizadores.EnrutadorMensaje;
import com.sideralsoft.interfaz.comunicadores.ControladorHTTP;
import com.sideralsoft.interfaz.readers.JsonReader;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;


public class SlaytherStrategy implements  EstrategiaProcesamiento{
    private EnrutadorMensaje enrutadorMensaje;
    private ControladorHTTP controladorHTTP;

    public SlaytherStrategy(){
        this.controladorHTTP = new ControladorHTTP();
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
                    return fields[8];
                }
            }
        }
        return null;
    }

    @Override
    public String validarMensaje(String clientAddress, String mensaje) throws IOException {
        String status;
        String[] lines = mensaje.split("(?=MSH|PID|OBR|OBX)");

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
        if(hasMSH && hasPID && hasOBR && hasOBX){
            status = "AA";
            estructurarJSON(clientAddress, mensaje);
            return "FINALIZADO";
        }else {
            status = "AE";
        }
        mensaje = generarRespuestaConfirmacion(mensaje, status);
        return mensaje;
    }

    private void estructurarJSON(String clientAddress, String mensaje) throws IOException {
        JsonReader jsonReader = JsonReader.getInstance();
        Equipo equipo = jsonReader.getEquipoByIp(clientAddress);
        Map<String, String> data = new LinkedHashMap<>();
        data.put("ip", clientAddress);
        data.put("id", equipo.getId());
        data.put("token", equipo.getToken());
        data.put("codigoEquipo", equipo.getCodigoEquipo());
        data.put("hl7Trama", mensaje);
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String json = gson.toJson(data);


        System.out.println("JSON serializado: " + json);
        controladorHTTP.enviarMensajeNube(json);
    }

    public void enviarRespuestaConfirmacion(String clientAddress, String mensaje) throws IOException {
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

        for (String segment : segments) {
            if (segment.startsWith("MSH")) {
                String[] fields = segment.split("\\|");

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

        String fixedDateTime = "202412241300";

        String ackControlId = "ACK-54321";

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
