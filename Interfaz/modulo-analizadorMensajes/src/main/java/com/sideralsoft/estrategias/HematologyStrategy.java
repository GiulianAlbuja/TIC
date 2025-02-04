package com.sideralsoft.estrategias;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sideralsoft.shared.entidades.Equipo;
import com.sideralsoft.shared.comunicadores.ControladorHTTP;
import com.sideralsoft.shared.estrategias.EstrategiaProcesamiento;
import com.sideralsoft.shared.readers.JsonReader;
import com.sideralsoft.shared.readers.YamlReader;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class HematologyStrategy implements EstrategiaProcesamiento {
    private ControladorHTTP controladorHTTP;

    public HematologyStrategy(){
        this.controladorHTTP = ControladorHTTP.getInstance();
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
        }else {
            status = "AE";
        }
        mensaje = generarRespuestaConfirmacion(mensaje, status);
        return mensaje;
    }

    private void estructurarJSON(String clientAddress, String mensaje) throws IOException {
        YamlReader yamlReader = YamlReader.getInstance();
        Equipo equipo = yamlReader.getEquipoByConfiguracionHl7("HematologyStrategy");
        Map<String, String> data = new LinkedHashMap<>();
        data.put("ip", equipo.getIp());
        //data.put("id", equipo.getId());
        data.put("id", equipo.getId());
        data.put("token", equipo.getToken());
        //data.put("codigoEquipo", equipo.getCodigoEquipo());
        data.put("configuracionHL7", equipo.getConfiguracionHl7());
        data.put("hl7Trama", mensaje);
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String json = gson.toJson(data);


        System.out.println("JSON serializado: - ACTUALIZACION 2 PRUEBA " + json);
        controladorHTTP.enviarMensajeNube(json);
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
