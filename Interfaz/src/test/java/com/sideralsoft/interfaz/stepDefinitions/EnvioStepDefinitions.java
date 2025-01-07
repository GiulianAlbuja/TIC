package com.sideralsoft.interfaz.stepDefinitions;

import com.sideralsoft.interfaz.ClientSession;
import com.sideralsoft.interfaz.TCPServer;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import java.io.IOException;
import java.net.Socket;

import static org.junit.Assert.assertTrue;

public class EnvioStepDefinitions{
    private TCPServer server;
    private ClientSession clientSession;

    public EnvioStepDefinitions(){
        this.server = new TCPServer();
    }

    @Dado("que la interfaz de comunicación ha iniciado una sesión con el equipo de laboratorio")
    public void queLaInterfazDeComunicaciónHaIniciadoUnaSesiónConElEquipoDeLaboratorio() throws IOException, InterruptedException {
            server.start();
            Socket clientSocket = new Socket("localhost", 3001);
            clientSession = new ClientSession(clientSocket, server);
            Thread.sleep(2000);
            System.out.println("Sesión activa: " + clientSession.estaSesionActiva());
            assertTrue("La sesión debería estar activa después de iniciar sesión.", clientSession.estaSesionActiva());
            clientSession.closeSession();
            System.out.println("Sesión activa: " + clientSession.estaSesionActiva());
    }

    @Cuando("la interfaz de comunicación reciba un mensaje ORU")
    public void laInterfazDeComunicaciónRecibaUnMensajeORU(String mensaje) throws IOException, InterruptedException {
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
