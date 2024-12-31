package com.sideralsoft.interfaz.servicios;

import com.sideralsoft.interfaz.ServicioConexion;
import org.junit.Test;
import static org.junit.Assert.*;

public class ServicioConexionTest {
    @Test
    public void testIniciarSesionConEquipo_Exito() {
        ServicioConexion servicioConexion = new ServicioConexion();
        servicioConexion.iniciarSesionConEquipo("Tipo3");
        assertTrue("La sesión debería estar activa después de iniciar sesión.", servicioConexion.estaLaSesionActiva());
    }
}
