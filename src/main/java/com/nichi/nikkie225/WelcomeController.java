package com.nichi.nikkie225;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

public class WelcomeController {

    @FXML
    private StackPane contentPane;

    @FXML
    private Button monitorButton;

    @FXML
    private Button tradeEntryButton;

    @FXML
    public void initialize() {
        try {
            loadView("hello-view.fxml");
            setActiveButton(monitorButton);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMonitorButton() throws IOException {
        loadView("hello-view.fxml");
        setActiveButton(monitorButton);
    }

    @FXML
    private void handleTradeEntryButton() {
        try {
            // Path to the external JAR (use "file:" prefix)
            String jarPath = "file:/C:/Users/nichiuser/Desktop/nichi-in-project/nikkie225/Nifty50Frontend.jar";

            // Create class loader with JAR
            URL[] urls = { new URL(jarPath) };
            ClassLoader jarClassLoader = new URLClassLoader(urls);

            // FXML path inside JAR (based on your jar content structure)
            URL fxmlUrl = jarClassLoader.getResource("com/nichi/nifty50frontend/TradeEntryView.fxml");

            if (fxmlUrl == null) {
                System.err.println("❌ FXML file not found inside the JAR.");
                return;
            }

            // Load FXML from the JAR using the custom class loader
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            loader.setClassLoader(jarClassLoader); // This is important!
            Node view = loader.load();

            // Replace content in the current StackPane
            contentPane.getChildren().setAll(view);
            setActiveButton(tradeEntryButton);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadView(String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Node view = loader.load();
        contentPane.getChildren().setAll(view);
    }

    private void setActiveButton(Button activeButton) {
        monitorButton.setDisable(false);
        tradeEntryButton.setDisable(false);
        activeButton.setDisable(true);
    }
}
