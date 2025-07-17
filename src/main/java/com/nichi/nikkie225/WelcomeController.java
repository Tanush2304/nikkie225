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
    private Button dmaGraphButton;

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TradeEntry.fxml"));
            Node view = loader.load();
            contentPane.getChildren().setAll(view);
            setActiveButton(tradeEntryButton);
        } catch (IOException e) {
            System.err.println("Error loading Trade Entry view: " + e.getMessage());
            e.printStackTrace();
        }

    }

    @FXML
    private void handleDmaGraphButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dma_graph.fxml"));
            Node view = loader.load();


            contentPane.getChildren().setAll(view);
            setActiveButton(dmaGraphButton);

        } catch (Exception e) {
            System.err.println("Error loading DMA Graph view: " + e.getMessage());
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
        dmaGraphButton.setDisable(false);

        activeButton.setDisable(true);
    }
}
