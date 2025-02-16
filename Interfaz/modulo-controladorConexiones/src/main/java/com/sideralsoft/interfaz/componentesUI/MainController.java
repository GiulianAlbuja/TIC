package com.sideralsoft.interfaz.componentesUI;

import com.sideralsoft.interfaz.comunicadores.TCPClient;
import com.sideralsoft.interfaz.comunicadores.TCPServer;
import com.sideralsoft.shared.entidades.Equipo;
import com.sideralsoft.shared.readers.YamlReader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class MainController implements ServerListener  {
    @FXML
    private TextArea textAreaReceived;  // Mensajes recibidos
    @FXML
    private TextArea textAreaSent;      // Mensajes enviados
    @FXML
    private TextArea textAreaConnection; // Estado de la conexión
    @FXML
    private Label etiquetasServidores;  // Etiqueta para mostrar información adicional
    @FXML
    private Button btnEscuchar;         // Botón "Escuchar"
    @FXML
    private Label puertoLabel;          // Puerto
    @FXML
    private Label ipLabel;
    @FXML
    private TabPane tabPane;// Dirección IP



    @FXML
    private void initialize() throws IOException {

        Map<String, Equipo> equipos = YamlReader.getInstance().getEquipos();
        for (Map.Entry<String, Equipo> entry : equipos.entrySet()) {
            String nombreEquipo = entry.getKey();
            Equipo equipo = entry.getValue();
            Tab tab = new Tab(nombreEquipo);
            AnchorPane anchorPane = createTabContent(equipo);
            tab.setContent(anchorPane);
            tabPane.getTabs().add(tab);
        }
    }

    private AnchorPane createTabContent(Equipo equipo) {
        // Crear un AnchorPane para el contenido de la pestaña
        AnchorPane anchorPane = new AnchorPane();

        // Crear los TextArea y otros componentes
        TextArea textAreaReceived = new TextArea();
        TextArea textAreaSent = new TextArea();
        TextArea textAreaConnection = new TextArea();
        textAreaConnection.setEditable(false);
        textAreaReceived.setEditable(false);
        textAreaSent.setEditable(false);

        // Crear los Labels
        Label labelReceived = new Label("Información recibida");
        Label labelSent = new Label("Información enviada");
        Label labelConnection = new Label("Estado conexión");


        Button btn = new Button("-");

        if (equipo.getTipoConexion().equals("cliente")) {
            TCPServer tcpServer = new TCPServer(equipo.getPuerto());
            btn = new Button("Escuchar");
            btn.setOnAction(e -> onEscucharButtonClick(textAreaConnection, equipo, tcpServer));
        } else if (equipo.getTipoConexion().equals("servidor")) {
            TCPClient tcpClient = null;
            btn = new Button("Conectar");
            btn.setOnAction(e -> onConectarButtonClick(textAreaConnection, equipo, tcpClient));
        }

        textAreaReceived.setLayoutX(24.0);
        textAreaReceived.setLayoutY(78.0);
        textAreaReceived.setPrefHeight(172.0);
        textAreaReceived.setPrefWidth(407.0);

        textAreaSent.setLayoutX(24.0);
        textAreaSent.setLayoutY(287.0);
        textAreaSent.setPrefHeight(209.0);
        textAreaSent.setPrefWidth(408.0);

        textAreaConnection.setLayoutX(473.0);
        textAreaConnection.setLayoutY(287.0);
        textAreaConnection.setPrefHeight(208.0);
        textAreaConnection.setPrefWidth(233.0);

        labelReceived.setLayoutX(23.0);
        labelReceived.setLayoutY(52.0);

        labelSent.setLayoutX(23.0);
        labelSent.setLayoutY(264.0);

        labelConnection.setLayoutX(473.0);
        labelConnection.setLayoutY(264.0);

        btn.setLayoutX(521.0);
        btn.setLayoutY(183.0);
        Label tipoConexionLabel = new Label("Tipo conexión: " + equipo.getTipoConexion());
        tipoConexionLabel.setLayoutX(479.0);
        tipoConexionLabel.setLayoutY(84.0);

        Label puertoLabel = new Label("Puerto: " + equipo.getPuerto());
        puertoLabel.setLayoutX(479.0);
        puertoLabel.setLayoutY(111.0);

        Label ipLabel = new Label("Dirección IP: " + equipo.getIp());
        ipLabel.setLayoutX(479.0);
        ipLabel.setLayoutY(138.0);

        anchorPane.getChildren().addAll(
                textAreaReceived, textAreaSent, textAreaConnection, labelReceived, labelSent,
                labelConnection, btn, tipoConexionLabel,puertoLabel, ipLabel
        );

        return anchorPane;
    }



    private void onEscucharButtonClick(TextArea textAreaConnection, Equipo equipo, TCPServer tcpServer) {
        if (tcpServer.isRunning()) {
            tcpServer.stopServer();
            textAreaConnection.setText("Desconectado...");
        } else {
            new Thread(tcpServer).start();
            textAreaConnection.setText("Escuchando...");
        }
    }

    private void offEscucharButtonClick( TCPServer tcpServer, TextArea textAreaConnection){
        textAreaConnection.setText("Desconectando...");
        tcpServer.stopServer();
    }

    private void onConectarButtonClick(TextArea textAreaConnection, Equipo equipo, TCPClient tcpClient){
        if (tcpClient == null) {

            tcpClient = new TCPClient(equipo.getIp(), equipo.getPuerto());
            textAreaConnection.setText("Conectando...");
        } else {
            tcpClient.closeConnection();
            textAreaConnection.setText("Desconectado...");
        }
    }

    @Override
    public void onMessageReceived(String message) {
        // Método para mostrar los mensajes recibidos
        textAreaReceived.appendText(message + "\n");
    }

    @Override
    public void onConnectionStatusChanged(String status) {
        // Método para mostrar el estado de la conexión
        textAreaConnection.setText(status);
    }

    @Override
    public void onError(String error) {

    }

    // Métodos adicionales para actualizar el `TextArea`
    public void updateReceivedMessage(String message) {
        textAreaReceived.appendText(message + "\n");
    }

    public void updateSentMessage(String message) {
        textAreaSent.appendText(message + "\n");
    }

    public void updateConnectionStatus(String status) {
        textAreaConnection.setText(status);
    }


}
