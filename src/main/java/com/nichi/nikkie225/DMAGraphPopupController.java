package com.nichi.nikkie225;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;

import java.sql.*;
import java.util.*;

public class DMAGraphPopupController {

    @FXML private ComboBox<String> stockSelector;
    @FXML private VBox chartContainer;
    @FXML private ScrollPane scrollPane;

    private Integer selectedDate = null;

    public void loadGraph(String code, Integer selectedDate) {
        this.selectedDate = selectedDate;
        initDropdown(code);
        clearCharts();
        drawGraph(code);
    }

    private void initDropdown(String currentCode) {
        Set<String> allStocks = new TreeSet<>();

        try (Connection conn = com.nichi.nikkie225.model.Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT code FROM priceriskfactor")) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                allStocks.add(rs.getString("code"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        stockSelector.setItems(FXCollections.observableArrayList(allStocks));
        stockSelector.setValue(currentCode);

        stockSelector.setOnAction(event -> {
            String selectedStock = stockSelector.getValue();
            if (selectedStock != null) {
                loadGraph(selectedStock, selectedDate);
            }
        });
    }

    private void clearCharts() {
        chartContainer.getChildren().clear();
    }

    private void drawGraph(String code) {
        XYChart.Series<String, Number> dma200Series = new XYChart.Series<>();
        dma200Series.setName("DMA200");
        XYChart.Series<String, Number> dma50Series = new XYChart.Series<>();
        dma50Series.setName("DMA50");
        XYChart.Series<String, Number> dma13Series = new XYChart.Series<>();
        dma13Series.setName("DMA13");
        XYChart.Series<String, Number> dma8Series = new XYChart.Series<>();
        dma8Series.setName("DMA8");
        XYChart.Series<String, Number> dma5Series = new XYChart.Series<>();
        dma5Series.setName("DMA5");

        String query = """
            SELECT dt, dma_200, dma_50, dma_13, dma_8, dma_5
            FROM priceriskfactor
            WHERE code = ?
            """ + (selectedDate != null ? "AND dt <= ? " : "") + """
            ORDER BY dt DESC
            LIMIT 30
            """;

        List<String> xCategories = new ArrayList<>();
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;

        try (Connection conn = com.nichi.nikkie225.model.Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, code);
            if (selectedDate != null) stmt.setInt(2, selectedDate);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String dateStr = String.valueOf(rs.getInt("dt"));
                xCategories.add(0, dateStr);

                double val200 = rs.getDouble("dma_200");
                double val50 = rs.getDouble("dma_50");
                double val13 = rs.getDouble("dma_13");
                double val8 = rs.getDouble("dma_8");
                double val5 = rs.getDouble("dma_5");

                dma200Series.getData().add(new XYChart.Data<>(dateStr, val200));
                dma50Series.getData().add(new XYChart.Data<>(dateStr, val50));
                dma13Series.getData().add(new XYChart.Data<>(dateStr, val13));
                dma8Series.getData().add(new XYChart.Data<>(dateStr, val8));
                dma5Series.getData().add(new XYChart.Data<>(dateStr, val5));

                for (double v : new double[]{val200, val50, val13, val8, val5}) {
                    if (v > 0) {
                        minY = Math.min(minY, v);
                        maxY = Math.max(maxY, v);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        double padding = (maxY - minY) * 0.1;
        minY -= padding;
        maxY += padding;

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");
        xAxis.setCategories(FXCollections.observableArrayList(xCategories));

        NumberAxis yAxis = new NumberAxis(minY, maxY, (maxY - minY) / 10);
        yAxis.setLabel("DMA Value");
        yAxis.setAutoRanging(false);

        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setAnimated(false);
        chart.setLegendVisible(true);
        chart.setCreateSymbols(true);
        chart.getData().addAll(dma200Series, dma50Series, dma13Series, dma8Series, dma5Series);

        chart.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        chart.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        chart.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        addTooltips(dma200Series);
        addTooltips(dma50Series);
        addTooltips(dma13Series);
        addTooltips(dma8Series);
        addTooltips(dma5Series);

        addMouseInteraction(chart, yAxis, minY, maxY);

        HBox controls = createZoomControls(chart, yAxis, minY, maxY);

        VBox chartBox = new VBox(10, controls, chart);
        chartBox.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        chartBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        VBox.setVgrow(chart, Priority.ALWAYS);
        VBox.setVgrow(chartBox, Priority.ALWAYS);

        chartContainer.getChildren().clear();
        chartContainer.getChildren().add(chartBox);
        VBox.setVgrow(chartContainer, Priority.ALWAYS);
    }

    private void addTooltips(XYChart.Series<String, Number> series) {
        for (XYChart.Data<String, Number> data : series.getData()) {
            Tooltip.install(data.getNode(), new Tooltip(series.getName() + ": " + data.getYValue() + "\nDate: " + data.getXValue()));
        }
    }

    private HBox createZoomControls(LineChart<String, Number> chart, NumberAxis yAxis, double minY, double maxY) {
        Button zoomIn = new Button("Zoom In");
        Button zoomOut = new Button("Zoom Out");
        Button reset = new Button("Reset Zoom");

        zoomIn.setOnAction(e -> {
            double range = yAxis.getUpperBound() - yAxis.getLowerBound();
            double center = (yAxis.getUpperBound() + yAxis.getLowerBound()) / 2;
            yAxis.setLowerBound(center - range * 0.4);
            yAxis.setUpperBound(center + range * 0.4);
        });

        zoomOut.setOnAction(e -> {
            double range = yAxis.getUpperBound() - yAxis.getLowerBound();
            double center = (yAxis.getUpperBound() + yAxis.getLowerBound()) / 2;
            yAxis.setLowerBound(center - range * 0.6);
            yAxis.setUpperBound(center + range * 0.6);
        });

        reset.setOnAction(e -> {
            yAxis.setLowerBound(minY);
            yAxis.setUpperBound(maxY);
        });

        return new HBox(10, zoomIn, zoomOut, reset);
    }

    private void addMouseInteraction(LineChart<String, Number> chart, NumberAxis yAxis, double minY, double maxY) {
        chart.setOnScroll((ScrollEvent event) -> {
            double deltaY = event.getDeltaY();
            double range = yAxis.getUpperBound() - yAxis.getLowerBound();
            double scaleFactor = deltaY > 0 ? 0.9 : 1.1;
            double center = (yAxis.getUpperBound() + yAxis.getLowerBound()) / 2;
            double newLower = center - range * scaleFactor / 2;
            double newUpper = center + range * scaleFactor / 2;

            if (newLower >= minY && newUpper <= maxY * 1.5) {
                yAxis.setLowerBound(newLower);
                yAxis.setUpperBound(newUpper);
            }
        });

        final double[] dragAnchor = new double[1];
        chart.setOnMousePressed((MouseEvent event) -> dragAnchor[0] = event.getY());

        chart.setOnMouseDragged((MouseEvent event) -> {
            double deltaY = event.getY() - dragAnchor[0];
            double shift = (yAxis.getUpperBound() - yAxis.getLowerBound()) * deltaY / chart.getHeight();
            yAxis.setLowerBound(yAxis.getLowerBound() + shift);
            yAxis.setUpperBound(yAxis.getUpperBound() + shift);
            dragAnchor[0] = event.getY();
        });
    }
}
