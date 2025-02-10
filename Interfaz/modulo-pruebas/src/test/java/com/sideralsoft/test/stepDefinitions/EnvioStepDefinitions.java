package com.sideralsoft.test.stepDefinitions;

import com.sideralsoft.shared.comunicadores.ControladorHTTP;
import com.sideralsoft.interfaz.comunicadores.Session;
import com.sideralsoft.interfaz.comunicadores.TCPServer;
import com.sideralsoft.test.mocks.MockTCPServer;
import io.cucumber.java.After;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;


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
    private String tipoEquipo;
    private ExecutorService executorService;
    private ControladorHTTP controladorHTTP;
    private MockTCPServer mockServer;

    public EnvioStepDefinitions() throws IOException {
        this.executorService = Executors.newCachedThreadPool();
    }

    @Dado("^que el equipo de laboratorio actúa como (.*)$")
    public void queElEquipoDeLaboratorioActúaComoTIPO_EQUIPO(String tipoEquipo) throws InterruptedException, IOException {
        if(tipoEquipo.equals("cliente")){
            this.tipoEquipo = tipoEquipo;
            this.server = TCPServer.getInstance();
            Thread.sleep(5000);
            server.start();
            Thread.sleep(5000);
            this.socket = new Socket("localhost", 3001);
        }else if(tipoEquipo.equals("servidor")){
            this.mockServer = MockTCPServer.getInstance();
            Thread.sleep(5000);
            mockServer.start();
            Thread.sleep(5000);
            this.tipoEquipo = tipoEquipo;
            Thread.sleep(10000);
            this.socket = new Socket("localhost", 3002);
        }
    }

    @Y("que la interfaz de comunicación ha iniciado una sesión con el equipo de laboratorio")
    public void queLaInterfazDeComunicaciónHaIniciadoUnaSesiónConElEquipoDeLaboratorio() {
        session = new Session(socket, tipoEquipo);
        if(tipoEquipo.equals("cliente")){
        }else if(tipoEquipo.equals("servidor")){
            executorService.execute(session);
        }
        System.out.println("Sesión activa: " + session.estaSesionActiva());
        assertTrue("La sesión debería estar activa después de iniciar sesión.", session.estaSesionActiva());
    }

    @Dado("que la interfaz de comunicación ha iniciado una sesión de tipo {string} con el equipo de laboratorio")
    public void queLaInterfazDeComunicaciónHaIniciadoUnaSesiónDeTipoConElEquipoDeLaboratorio(String tipoSession) throws InterruptedException, IOException {
        this.tipoEquipo = tipoSession;
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
        if (tipoEquipo.equals("cliente")){
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(mensaje);
            Thread.sleep(5000);
        }else if(tipoEquipo.equals("servidor")){
            Socket clientSocket = mockServer.getClientSocket();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println(mensaje);
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
        System.out.println("Mensajes enviados GHERKIN:"+ mensaje);
        assertTrue("El mensaje enviado no es el esperado.", mensajesEnviados.contains(mensaje));
    }

    @Y("envía los resultados clínicos a Orión")
    public void envíaLosResultadosClínicosAOrión(String resultados) throws InterruptedException {
        Thread.sleep(5000);
        controladorHTTP = ControladorHTTP.getInstance();
        List<String> mensajesEnviados = controladorHTTP.getMensajesEnviados();
        System.out.println("Mensajes enviados a Orion:"+ mensajesEnviados);
        assertTrue("El mensaje enviado a Orion no es el esperado.", mensajesEnviados.contains(resultados));
    }

    @After
    public void tearDown() throws IOException, InterruptedException {
        if (session != null) {
            session.closeSession();
        }
        if (server != null) {
            server.stopServer();
            Thread.sleep(2000);
        }
        if (mockServer != null) {
            mockServer.stopServer();
            Thread.sleep(2000);
        }
        if (socket != null) {
            socket.close();
        }
        executorService.shutdown();
        Thread.sleep(5000);
    }


    @Cuando("la interfaz de comunicación reciba un mensaje de consulta QRY")
    public void laInterfazDeComunicaciónRecibaUnMensajeDeConsultaQRY(String mensaje) throws InterruptedException, IOException {
        Thread.sleep(5000);
        if (tipoEquipo.equals("cliente")){
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(mensaje);
            Thread.sleep(5000);
        }else if(tipoEquipo.equals("servidor")){
            Socket clientSocket = mockServer.getClientSocket();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println(mensaje);
        }
        Thread.sleep(5000);
        List<String> mensajesRecibidos = session.getMensajesRecibidos();
        System.out.println("Mensaje:"+ mensajesRecibidos);
        assertTrue("El mensaje no fue recibido correctamente por el servidor.", mensajesRecibidos.contains(mensaje));
    }


    @Entonces("la interfaz de comunicación envía al equipo de laboratorio una respuesta de confirmación QCK")
    public void laInterfazDeComunicaciónEnvíaAlEquipoDeLaboratorioUnaRespuestaDeConfirmaciónQCK(String mensaje) {
        List<String> mensajesEnviados = session.getMensajesEnviados();
        System.out.println("Mensajes enviados:"+ mensajesEnviados);
        System.out.println("Mensajes enviados GHERKIN:"+ mensaje);
        assertTrue("El mensaje enviado no es el esperado.", mensajesEnviados.contains(mensaje));
    }

    @Y("envía la consulta de órdenes pendientes a Orión")
    public void envíaLaConsultaDeÓrdenesPendientesAOrión(String consultaOrden) throws InterruptedException {
        Thread.sleep(5000);
        controladorHTTP = ControladorHTTP.getInstance();
        List<String> mensajesEnviados = controladorHTTP.getMensajesEnviados();
        System.out.println("Mensajes enviados a Orion:"+ mensajesEnviados);
        assertTrue("El mensaje enviado a Orion no es el esperado.", mensajesEnviados.contains(consultaOrden));
    }

    @Y("envía la información de muestra al equipo de laboratorio")
    public void envíaLaInformaciónDeMuestraAlEquipoDeLaboratorio(String informacionMuestra) {
        List<String> mensajesEnviados = session.getMensajesEnviados();
        System.out.println("Mensajes enviados:"+ mensajesEnviados);
        System.out.println("Mensajes enviados GHERKIN:"+ informacionMuestra);
        assertTrue("El mensaje enviado no es el esperado.", mensajesEnviados.contains(informacionMuestra));
    }
}


