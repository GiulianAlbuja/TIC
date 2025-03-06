package com.sideralsoft.test.stepDefinitions;

import com.sideralsoft.interfaz.comunicadores.TCPActor;
import com.sideralsoft.interfaz.comunicadores.TCPClient;
import com.sideralsoft.shared.comunicadores.ControladorHTTP;
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

public class StepDefinitions {
    private TCPServer server;
    private TCPClient client;
    private Socket socket;
    private String tipoConexion;
    private ExecutorService executorService;
    private ControladorHTTP controladorHTTP;
    private MockTCPServer mockServer;

    public StepDefinitions() throws IOException {
        this.executorService = Executors.newCachedThreadPool();
    }

    @Dado("^que el equipo de laboratorio actúa como (.*)$")
    public void queElEquipoDeLaboratorioActúaComoTIPO_EQUIPO(String tipoConexion) throws InterruptedException, IOException {
        if(tipoConexion.equals("cliente")){
            this.tipoConexion = tipoConexion;
            this.server = new TCPServer(3001);
            Thread.sleep(5000);
            new Thread(server).start();
            Thread.sleep(5000);
            this.socket = new Socket("localhost", 3001);
        }else if(tipoConexion.equals("servidor")){
            this.mockServer = MockTCPServer.getInstance();
            Thread.sleep(5000);
            mockServer.start();
            Thread.sleep(5000);
            this.tipoConexion = tipoConexion;
            Thread.sleep(10000);
            this.client = new TCPClient("localhost", 3002);
        }
    }

    //ENVÍO DE RESULTADOS CLÍNICOS

    @Cuando("la interfaz de comunicación reciba un mensaje ORU")
    public void laInterfazDeComunicaciónRecibaUnMensajeORU(String mensaje) throws IOException, InterruptedException {
        Thread.sleep(5000);
        List<String> mensajesRecibidos = null;
        if (tipoConexion.equals("cliente")){
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(mensaje);
            Thread.sleep(5000);
            mensajesRecibidos = server.getMensajesRecibidos();
        }else if(tipoConexion.equals("servidor")){
            Socket clientSocket = mockServer.getClientSocket();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println(mensaje);
            out.close();
            mensajesRecibidos = client.getMensajesRecibidos();
        }
        Thread.sleep(8000);
        System.out.println("Mensaje:"+ mensajesRecibidos);
        assertTrue("El mensaje no fue recibido correctamente por el servidor.", mensajesRecibidos.contains(mensaje));
    }

    @Entonces("la interfaz de comunicación envía al equipo de laboratorio una respuesta de confirmación ACK")
    public void laInterfazDeComunicaciónEnvíaAlEquipoDeLaboratorioUnaRespuestaDeConfirmaciónACK(String mensaje) throws InterruptedException {
        Thread.sleep(8000);
        List<String>  mensajesEnviados = null;
        if (tipoConexion.equals("cliente")){
            mensajesEnviados = server.getMensajesEnviados();
        }else if(tipoConexion.equals("servidor")){
            mensajesEnviados = client.getMensajesEnviados();
        }
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

    //CONSULTA DE ÓRDENES PENDIENTES

    @Cuando("la interfaz de comunicación reciba un mensaje de consulta QRY")
    public void laInterfazDeComunicaciónRecibaUnMensajeDeConsultaQRY(String mensaje) throws InterruptedException, IOException {
        Thread.sleep(5000);
        List<String> mensajesRecibidos = null;
        if (tipoConexion.equals("cliente")){
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(mensaje);
            Thread.sleep(5000);
            mensajesRecibidos = server.getMensajesRecibidos();
        }else if(tipoConexion.equals("servidor")){
            Socket clientSocket = mockServer.getClientSocket();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println(mensaje);
            out.close();
            mensajesRecibidos = client.getMensajesRecibidos();
        }
        Thread.sleep(5000);
        System.out.println("Mensaje:"+ mensajesRecibidos);
        assertTrue("El mensaje no fue recibido correctamente por el servidor.", mensajesRecibidos.contains(mensaje));
    }

    @Entonces("la interfaz de comunicación envía al equipo de laboratorio una respuesta de confirmación QCK")
    public void laInterfazDeComunicaciónEnvíaAlEquipoDeLaboratorioUnaRespuestaDeConfirmaciónQCK(String mensaje) {
        List<String>  mensajesEnviados = null;
        if (tipoConexion.equals("cliente")){
            mensajesEnviados = server.getMensajesEnviados();
        }else if(tipoConexion.equals("servidor")){
            mensajesEnviados = client.getMensajesEnviados();
        }
        System.out.println("Mensajes enviados:"+ mensajesEnviados);
        System.out.println("Mensajes enviados GHERKIN:"+ mensaje);
        assertTrue("El mensaje enviado no es el esperado.", mensajesEnviados.contains(mensaje));
    }

    @Y("envía la consulta de órdenes pendientes a Orión")
    public void envíaLaConsultaDeÓrdenesPendientesAOrión(String consultaOrden) throws InterruptedException {
        Thread.sleep(5000);
        controladorHTTP = ControladorHTTP.getInstance();
        List<String> mensajesEnviados = controladorHTTP.getMensajesEnviados();
        //System.out.println("Mensajes enviados a Orion:"+ mensajesEnviados);
        assertTrue("El mensaje enviado a Orion no es el esperado.", mensajesEnviados.contains(consultaOrden));
    }

    @Y("envía la información de muestra al equipo de laboratorio")
    public void envíaLaInformaciónDeMuestraAlEquipoDeLaboratorio(String informacionMuestra) {
        List<String>  mensajesEnviados = null;
        if (tipoConexion.equals("cliente")){
            mensajesEnviados = server.getMensajesEnviados();
        }else if(tipoConexion.equals("servidor")){
            mensajesEnviados = client.getMensajesEnviados();
        }
        System.out.println("Mensajes enviados:"+ mensajesEnviados);
        System.out.println("Mensajes enviados GHERKIN:"+ informacionMuestra);
        assertTrue("El mensaje enviado no es el esperado.", mensajesEnviados.contains(informacionMuestra));
    }

    @After
    public void tearDown() throws IOException, InterruptedException {
        if (server != null) {
            server.stopServer();
            Thread.sleep(2000);
        }
        if (mockServer != null) {
            mockServer.stopServer();
            Thread.sleep(2000);
        }
        Thread.sleep(5000);
    }

}


