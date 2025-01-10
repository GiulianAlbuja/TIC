package com.sideralsoft.interfaz.stepDefinitions;

import com.sideralsoft.interfaz.comunicadores.ClientSession;
import com.sideralsoft.interfaz.comunicadores.TCPServer;
import io.cucumber.java.Before;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class EnvioStepDefinitions{
    private TCPServer server;
    private ClientSession clientSession;
    private Socket clientSocket;

    public EnvioStepDefinitions() throws IOException {
    }

    @Before
    public void setup() throws IOException, InterruptedException {
        this.server = TCPServer.getInstance();
        Thread.sleep(5000);
        server.start();  // Iniciar el servidor antes de cada escenario
        this.clientSocket = new Socket("localhost", 3001);  // Crear un nuevo cliente
    }

    @Dado("que la interfaz de comunicación ha iniciado una sesión con el equipo de laboratorio")
    public void queLaInterfazDeComunicaciónHaIniciadoUnaSesiónConElEquipoDeLaboratorio() throws IOException, InterruptedException {
        clientSession = new ClientSession(clientSocket, server);
        System.out.println("Sesión activa: " + clientSession.estaSesionActiva());
        assertTrue("La sesión debería estar activa después de iniciar sesión.", clientSession.estaSesionActiva());
    }

    @Cuando("la interfaz de comunicación reciba un mensaje ORU")
    public void laInterfazDeComunicaciónRecibaUnMensajeORU(String mensaje) throws IOException, InterruptedException {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        out.println(mensaje);
        Thread.sleep(5000);
        List<String> mensajesRecibidos = server.getMensajesRecibidos();
        System.out.println("Mensaje:"+ mensajesRecibidos);
        assertTrue("El mensaje no fue recibido correctamente por el servidor.", mensajesRecibidos.contains("Cliente" + " [" + clientSocket.getLocalAddress() + "]: " + mensaje));
    }

    @Entonces("la interfaz de comunicación envía al equipo de laboratorio una respuesta de confirmación ACK")
    public void laInterfazDeComunicaciónEnvíaAlEquipoDeLaboratorioUnaRespuestaDeConfirmaciónACK(String mensaje) throws InterruptedException, IOException {
        List<String> mensajesEnviados = server.getMensajesEnviados();
        System.out.println("Mensajes enviados:"+ mensajesEnviados);
        assertTrue("El mensaje enviado no es el esperado.", mensajesEnviados.contains(mensaje));
        clientSession.closeSession();
        server.stopServer();
        Thread.sleep(5000);

    }

    @Y("envía los resultados clínicos a Orion")
    public void envíaLosResultadosClínicosAOrion(String resultados) {

    }
}
