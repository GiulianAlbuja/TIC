package com.sideralsoft.interfaz;

import com.sideralsoft.interfaz.componentesUI.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/index.fxml"));

        AnchorPane root = loader.load();

        MainController controller = loader.getController();

        Scene scene = new Scene(root);
        primaryStage.setTitle("Interfaz de comunicación TCP/IP");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}