package com.nichi.nikkie225;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("welcomepage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        stage.setTitle("NichiIn PL Monitor Tool");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }
}
