package com.sideralsoft.shared.entidades;

import java.util.List;

public class Equipo {
    private String nombre;
    private String id;
    private String tipoConexion;
    private String ip;
    private int puerto;
    private String configuracionHl7;
    private String token;
    private List<String> camposIdentificadores;

    public Equipo(String nombre, String id, String ip, String tipoConexion, int puerto, String configuracionHl7, String token, List<String> camposIdentificadores) {
        this.nombre = nombre;
        this.id = id;
        this.ip = ip;
        this.tipoConexion = tipoConexion;
        this.puerto = puerto;
        this.configuracionHl7 = configuracionHl7;
        this.token = token;
        this.camposIdentificadores = camposIdentificadores;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public String getId() { return id; }
    public String getIp() { return ip; }
    public String getTipoConexion() { return tipoConexion; }
    public int getPuerto() { return puerto; }
    public String getConfiguracionHl7() { return configuracionHl7; }
    public String getToken() { return token; }
    public List<String> getCamposIdentificadores() { return camposIdentificadores; }

    @Override
    public String toString() {
        return "Equipo{" +
                "nombre='" + nombre + '\'' +
                ", id=" + id +
                ", ip='" + ip + '\'' +
                ", tipoConexion='" + tipoConexion + '\'' +
                ", puerto=" + puerto +
                ", configuracionHl7='" + configuracionHl7 + '\'' +
                ", token='" + token + '\'' +
                ", camposIdentificadores=" + camposIdentificadores +
                '}';
    }
}
