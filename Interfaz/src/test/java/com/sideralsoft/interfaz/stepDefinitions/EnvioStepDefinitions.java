package com.sideralsoft.interfaz.stepDefinitions;

import com.sideralsoft.interfaz.ServicioConexion;
import io.cucumber.java.eo.Se;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;

import static org.junit.Assert.assertTrue;

public class EnvioStepDefinitions {
    private ServicioConexion servicioConexion;

    public EnvioStepDefinitions(){
        this.servicioConexion = new ServicioConexion();
    }

    @Dado("que la interfaz de comunicación ha iniciado una sesión con el equipo de laboratorio {string}")
    public void queLaInterfazDeComunicaciónHaIniciadoUnaSesiónConElEquipoDeLaboratorio(String arg0) {
        servicioConexion.iniciarSesionConEquipo(arg0);
        assertTrue("La sesión debería estar activa después de iniciar sesión.", servicioConexion.estaLaSesionActiva());
    }

    @Cuando("la interfaz de comunicación reciba un mensaje {string}")
    public void laInterfazDeComunicaciónRecibaUnMensaje(String arg0) {

    }

    @Entonces("la interfaz de comunicación procesa el mensaje {string}")
    public void laInterfazDeComunicaciónProcesaElMensaje(String arg0) {

    }

    @Y("genera una respuesta de confirmación {string}")
    public void generaUnaRespuestaDeConfirmación(String arg0) {

    }

    @Y("el estado de envío de resultados clínicos está en {string}")
    public void elEstadoDeEnvíoDeResultadosClínicosEstáEn(String arg0) {

    }
}
