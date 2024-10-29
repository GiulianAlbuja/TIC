package com.sideralsoft.interfaz;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Javafx extends Application {

    private TextArea messageArea;
    private TextField inputField;
    private TCPServer server;

    @Override
    public void start(Stage stage) {
        // Configuración de la interfaz
        messageArea = new TextArea();
        messageArea.setEditable(false);
        inputField = new TextField();
        Button sendButton = new Button("Enviar");

        // Configurar el envío de mensajes
        sendButton.setOnAction(e -> sendMessage());

        VBox layout = new VBox(10, messageArea, inputField, sendButton);
        Scene scene = new Scene(layout, 640, 480);
        stage.setScene(scene);
        stage.setTitle("Servidor TCP con JavaFX");
        stage.show();

        // Iniciar el servidor TCP
        server = new TCPServer(messageArea);
        server.start();
    }

    private void sendMessage() {
        String message = inputField.getText();
        if (!message.isEmpty()) {
            server.sendMessageToClient(message);
            messageArea.appendText("Servidor: " + message + "\n");
            inputField.clear();
        }
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        server.stopServer();
    }
}
