package com.sideralsoft.interfaz;

public class ServicioConexion {
    private boolean sesionActiva = false;

    public void iniciarSesionConEquipo(String equipoLaboratorio) {
        if (equipoLaboratorio == null || equipoLaboratorio.isEmpty()) {
            throw new IllegalArgumentException("El equipo de laboratorio no puede ser nulo o vacío.");
        }
        // Simula la conexión
        System.out.println("Conectando con el equipo: " + equipoLaboratorio);
        sesionActiva = true; // Cambia el estado a activo
    }

    public boolean estaLaSesionActiva() {
        return sesionActiva;
    }

}
