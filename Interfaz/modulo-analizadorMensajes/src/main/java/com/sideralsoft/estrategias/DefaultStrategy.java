package com.sideralsoft.estrategias;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sideralsoft.shared.entidades.Equipo;
import com.sideralsoft.shared.comunicadores.ControladorHTTP;
import com.sideralsoft.shared.estrategias.EstrategiaProcesamiento;
import com.sideralsoft.shared.readers.YamlReader;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultStrategy implements EstrategiaProcesamiento {
    private ControladorHTTP controladorHTTP;

    public DefaultStrategy(){
        this.controladorHTTP = ControladorHTTP.getInstance();
    }

    @Override
    public String analizarTipoMensaje(String mensaje) {
        String[] segments = mensaje.split("\\\\r");
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
    public String validarMensajeORU(String clientAddress, String mensaje) throws IOException {
        String status;
        String json = "";
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
            json = estructurarJSON(mensaje);
            controladorHTTP.enviarResultadosClinicosANube(json);
        }else {
            status = "AE";
        }
        mensaje = generarRespuestaACK(mensaje, status);
        return mensaje;
    }
    @Override
    public Map<String, String> validarMensajeQRY(String clientAddress, String mensaje) throws IOException {
        Map<String, String> data = new LinkedHashMap<>();
        String status;
        String json = "";
        StringBuilder jsonResponse = new StringBuilder();
        String orden = "";
        String[] lines = mensaje.split("(?=MSH|QRD|RES)");

        boolean hasMSH = false;
        boolean hasQRD = false;
        boolean hasRES = false;

        for (String line : lines) {
            if (line.startsWith("MSH")) {
                hasMSH = true;
            } else if (line.startsWith("QRD")) {
                hasQRD = true;
            } else if (line.startsWith("RES")) {
                hasRES = true;
            }
        }
        if(hasMSH && hasQRD && hasRES){
            status = "AA";
            json = estructurarJSON(mensaje);
            jsonResponse = controladorHTTP.enviarConsultaDeOrdenANube(json);
            data.put("DSR",obtenerOrden(jsonResponse));
        }else {
            status = "AE";
        }
        data.put("QCK",generarRespuestaQCK(mensaje, status));
        return data;
    }

    private String obtenerOrden(StringBuilder jsonResponse) {
        String orden = "";
        Gson gson = new GsonBuilder().create();
        Map<String, String> map = gson.fromJson(jsonResponse.toString(), Map.class);
        orden = map.get("orden");
        System.out.println("orden" + orden);
        return orden;
    }

    private String generarRespuestaQCK(String mensaje, String status) {
        String[] segments = mensaje.split("\\\\r");

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



        String fixedDateTime = "202402061201";

        String ackControlId = "QCK-654321";

        StringBuilder ackBuilder = new StringBuilder();
        ackBuilder.append("MSH|^~\\&|")
                .append(receivingApplication).append("|")
                .append(receivingFacility).append("|")
                .append(sendingApplication).append("|")
                .append(sendingFacility).append("|")
                .append(fixedDateTime).append("||QCK|")
                .append(ackControlId).append("|")
                .append(processingId).append("|")
                .append(version).append("|")
                .append("MSA|"+status+"|").append(messageControlId).append("|");
        return ackBuilder.toString();
    }

    private String estructurarJSON(String mensaje) throws IOException {
        YamlReader yamlReader = YamlReader.getInstance();
        Equipo equipo = yamlReader.getEquipoByConfiguracionHl7("DefaultStrategy");
        Map<String, String> data = new LinkedHashMap<>();
        data.put("ip", equipo.getIp());
        //data.put("id", equipo.getId());
        data.put("id", equipo.getId());
        data.put("token", equipo.getToken());
        data.put("estrategiaHL7", equipo.getConfiguracionHl7());
        data.put("hl7Trama", mensaje);
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String json = gson.toJson(data);
        System.out.println("JSON serializado: - ACTUALIZACION 2 PRUEBA " + json);
        return json;
    }


    public String generarRespuestaACK(String mensaje, String status) {
        String[] segments = mensaje.split("\\\\r");

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
