package com.sideralsoft.interfaz.stepDefinitions;

import com.sideralsoft.interfaz.comunicadores.Session;
import com.sideralsoft.interfaz.comunicadores.TCPServer;
import io.cucumber.java.After;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;

import org.junit.Before;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertTrue;

public class EnvioStepDefinitions{
    private TCPServer server;
    private Session session;
    private Socket socket;
    private String tipoSession;
    private ExecutorService executorService;

    public EnvioStepDefinitions() throws IOException {
        this.executorService = Executors.newCachedThreadPool();
    }

    @Before
    public void setUp() throws IOException, InterruptedException {
        Thread.sleep(5000);
    }

    @Dado("que el equipo de laboratorio actúa como cliente")
    public void queElEquipoDeLaboratorioActúaComoCliente() throws InterruptedException, IOException {
        this.server = TCPServer.getInstance();
        Thread.sleep(5000);
        server.start();
        Thread.sleep(5000);
        this.socket = new Socket("localhost", 3001);
    }

    @Dado("que el equipo de laboratorio actúa como servidor")
    public void queElEquipoDeLaboratorioActúaComoServidor() throws IOException, InterruptedException {
        Thread.sleep(10000);
        this.socket = new Socket("localhost", 3002);
    }

    @Dado("que la interfaz de comunicación ha iniciado una sesión de tipo {string} con el equipo de laboratorio")
    public void queLaInterfazDeComunicaciónHaIniciadoUnaSesiónDeTipoConElEquipoDeLaboratorio(String tipoSession) throws InterruptedException, IOException {
        this.tipoSession = tipoSession;
        session = new Session(socket, tipoSession);
        if(tipoSession.equals("cliente")){
        }else if(tipoSession.equals("servidor")){
            executorService.execute(session);
        }
        System.out.println("Sesión activa: " + session.estaSesionActiva());
        assertTrue("La sesión debería estar activa después de iniciar sesión.", session.estaSesionActiva());
    }

    @Cuando("la interfaz de comunicación reciba un mensaje ORU")
    public void laInterfazDeComunicaciónRecibaUnMensajeORU(String mensaje) throws IOException, InterruptedException {
        Thread.sleep(5000);
        if (tipoSession.equals("cliente")){
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(mensaje);
            Thread.sleep(5000);
        }
        Thread.sleep(5000);
        List<String> mensajesRecibidos = session.getMensajesRecibidos();
        System.out.println("Mensaje:"+ mensajesRecibidos);
        assertTrue("El mensaje no fue recibido correctamente por el servidor.", mensajesRecibidos.contains(mensaje));
    }

    @Entonces("la interfaz de comunicación envía al equipo de laboratorio una respuesta de confirmación ACK")
    public void laInterfazDeComunicaciónEnvíaAlEquipoDeLaboratorioUnaRespuestaDeConfirmaciónACK(String mensaje) throws InterruptedException, IOException {
        List<String> mensajesEnviados = session.getMensajesEnviados();
        System.out.println("Mensajes enviados:"+ mensajesEnviados);
        assertTrue("El mensaje enviado no es el esperado.", mensajesEnviados.contains(mensaje));
    }

    @Y("envía los resultados clínicos a Orion")
    public void envíaLosResultadosClínicosAOrion(String resultados) throws IOException, InterruptedException {
    }

    @After
    public void tearDown() throws IOException, InterruptedException {
        if (session != null) {
            session.closeSession();
        }
        if (server != null) {
            server.stopServer();
        }
        if (socket != null) {
            socket.close();
        }
        executorService.shutdown();
    }
}


