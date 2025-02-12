package com.sideralsoft.interfaz;

import com.sideralsoft.interfaz.comunicadores.ServerListener;
import com.sideralsoft.interfaz.comunicadores.TCPClient;
import com.sideralsoft.interfaz.comunicadores.TCPServer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Javafx extends Application implements ServerListener {
    private TextArea messageArea;
    private TextField inputField;
    private TextField clientAddressField;
    private TCPServer server;
    private TCPClient client;

    @Override
    public void start(Stage stage) {
        messageArea = new TextArea();
        messageArea.setEditable(false);
        inputField = new TextField();
        clientAddressField = new TextField();
        clientAddressField.setPromptText("Dirección del cliente (ej. /127.0.0.1)");
        Button sendButton = new Button("Enviar");

        sendButton.setOnAction(e -> sendMessageToClient());

        VBox layout = new VBox(10, messageArea, clientAddressField, inputField, sendButton);
        Scene scene = new Scene(layout, 640, 480);
        stage.setScene(scene);
        stage.setTitle("Servidor TCP con JavaFX");
        stage.show();
        client = new TCPClient("localhost", 3002);

        //server = new TCPServer("3001");
        //server.addServerListener(this);
        //server.start();
    }

    private void sendMessageToClient() {
        String message = inputField.getText();
        String clientAddress = clientAddressField.getText();

        if (!message.isEmpty() && !clientAddress.isEmpty()) {
            //server.sendMessageToClient(clientAddress, message);  // Enviar solo al cliente indicado
            messageArea.appendText("Servidor (a " + clientAddress + "): " + message + "\n");
            inputField.clear();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        server.stopServer();
    }

    // Implementación de los métodos del ServerListener
    @Override
    public void onMessageReceived(String message) {
        Platform.runLater(() -> messageArea.appendText(message + "\n"));
    }

    @Override
    public void onClientConnected(String clientInfo) {
        Platform.runLater(() -> messageArea.appendText(clientInfo + "\n"));
    }

    @Override
    public void onError(String error) {
        Platform.runLater(() -> messageArea.appendText("Error: " + error + "\n"));
    }

    public static void main(String[] args) {
        launch();
    }
}
