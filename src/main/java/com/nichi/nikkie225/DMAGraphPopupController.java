package com.nichi.nikkie225;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.nichi.nikkie225.model.Database;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Window;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DMAGraphPopupController {

    @FXML private ComboBox<String> stockSelector;
    @FXML private DatePicker datePicker;
    @FXML private Pane canvasContainer;
    @FXML private Button exportPdfButton;
    @FXML private Button exportHtmlButton;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    private WebView currentWebView;
    private String currentStockCode;
    private String fromDt;
    private String toDt;

    public void initialize() {
        loadStockSymbols();
        stockSelector.setOnAction(e -> loadGraphForSelection());
        datePicker.setOnAction(e -> loadGraphForSelection());
        exportPdfButton.setOnAction(e -> {
            try {
                exportCurrentChartToPDF();
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Error", "Export failed: " + ex.getMessage());
            }
        });
        exportHtmlButton.setOnAction(e -> exportCurrentChartToHTML());
    }

    private void loadStockSymbols() {
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT code FROM priceriskfactor ORDER BY code")) {
            ResultSet rs = stmt.executeQuery();
            List<String> stocks = new ArrayList<>();
            while (rs.next()) {
                stocks.add(rs.getString("code"));
            }
            stockSelector.getItems().addAll(stocks);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadGraphForSelection() {
        String selectedStock = stockSelector.getValue();
        LocalDate selectedDate = datePicker.getValue();
        if (selectedStock != null && selectedDate != null) {
            int selectedDt = Integer.parseInt(selectedDate.format(formatter));
            loadGraph(selectedStock, selectedDt);
        }
    }

    public void loadGraph(String stockCode, Integer selectedDate) {
        try (Connection conn = Database.getConnection()) {
            LocalDate selected = LocalDate.parse(String.valueOf(selectedDate), formatter);
            int fromDate = Integer.parseInt(selected.minusDays(100).format(formatter));

            String dmaSql = """
                SELECT dt, dma_5, dma_8, dma_13, dma_50, dma_200
                FROM priceriskfactor
                WHERE code = ? AND dt BETWEEN ? AND ?
                ORDER BY dt ASC
            """;

            PreparedStatement dmaStmt = conn.prepareStatement(dmaSql);
            dmaStmt.setString(1, stockCode);
            dmaStmt.setInt(2, fromDate);
            dmaStmt.setInt(3, selectedDate);
            ResultSet rs = dmaStmt.executeQuery();

            List<String> dates = new ArrayList<>();
            List<Double> dma5 = new ArrayList<>();
            List<Double> dma8 = new ArrayList<>();
            List<Double> dma13 = new ArrayList<>();
            List<Double> dma50 = new ArrayList<>();
            List<Double> dma200 = new ArrayList<>();

            while (rs.next()) {
                dates.add(String.valueOf(rs.getInt("dt")));
                dma5.add(rs.getDouble("dma_5"));
                dma8.add(rs.getDouble("dma_8"));
                dma13.add(rs.getDouble("dma_13"));
                dma50.add(rs.getDouble("dma_50"));
                dma200.add(rs.getDouble("dma_200"));
            }

            List<Double> open = new ArrayList<>();
            List<Double> high = new ArrayList<>();
            List<Double> low = new ArrayList<>();
            List<Double> close = new ArrayList<>();

            String ohlcSql = """
                SELECT dt, openprice, highprice, lowprice, closeprice
                FROM historicalpricelist
                WHERE code_id = ? AND dt BETWEEN ? AND ?
                ORDER BY dt ASC
            """;

            PreparedStatement ohlcStmt = conn.prepareStatement(ohlcSql);
            ohlcStmt.setString(1, stockCode);
            ohlcStmt.setInt(2, fromDate);
            ohlcStmt.setInt(3, selectedDate);

            ResultSet ohlcRs = ohlcStmt.executeQuery();
            while (ohlcRs.next()) {
                String date = String.valueOf(ohlcRs.getInt("dt"));
                if (!dates.contains(date)) continue;
                open.add(ohlcRs.getDouble("openprice"));
                high.add(ohlcRs.getDouble("highprice"));
                low.add(ohlcRs.getDouble("lowprice"));
                close.add(ohlcRs.getDouble("closeprice"));
            }

            showPlotlyGraph(dates, dma5, dma8, dma13, dma50, dma200, open, high, low, close, stockCode);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showPlotlyGraph(List<String> dates, List<Double> dma5, List<Double> dma8,
                                 List<Double> dma13, List<Double> dma50, List<Double> dma200,
                                 List<Double> open, List<Double> high, List<Double> low, List<Double> close,
                                 String stockCode) {

        String html = PlotlyGraphGenerator.generateDMAHTML(dates, dma5, dma8, dma13, dma50, dma200,
                open, high, low, close, stockCode);

        WebView webView = new WebView();
        double width = Screen.getPrimary().getBounds().getWidth();
        double height = Screen.getPrimary().getBounds().getHeight();
        webView.setPrefSize(width, height);
        webView.getEngine().loadContent(html);
        canvasContainer.getChildren().clear();
        canvasContainer.getChildren().add(webView);

        this.currentWebView = webView;
        this.currentStockCode = stockCode;
        this.fromDt = dates.isEmpty() ? "NA" : dates.get(0);
        this.toDt = dates.isEmpty() ? "NA" : dates.get(dates.size() - 1);
    }

    private void exportCurrentChartToPDF() throws Exception {
        if (currentWebView == null || currentStockCode == null) return;

        Object widthObj = currentWebView.getEngine().executeScript("document.body.scrollWidth");
        Object heightObj = currentWebView.getEngine().executeScript("document.body.scrollHeight");
        int contentWidth = (widthObj instanceof Number) ? ((Number) widthObj).intValue() : 1200;
        int contentHeight = (heightObj instanceof Number) ? ((Number) heightObj).intValue() : 800;
        currentWebView.setPrefSize(contentWidth, contentHeight);
        currentWebView.resize(contentWidth, contentHeight);

        WritableImage snapshot = currentWebView.snapshot(new SnapshotParameters(), null);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(snapshot, null);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save DMA Graph PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName("Dmagraph_" + currentStockCode + "_" + fromDt + "-" + toDt + ".pdf");

        Window window = exportPdfButton.getScene().getWindow();
        java.io.File selectedFile = fileChooser.showSaveDialog(window);
        if (selectedFile == null) return;

        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(selectedFile));
        document.open();

        ByteArrayOutputStream imageBytes = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", imageBytes);
        Image pdfImage = Image.getInstance(imageBytes.toByteArray());

        pdfImage.scaleToFit(PageSize.A4.getHeight() - 40, PageSize.A4.getWidth() - 40);
        pdfImage.setAlignment(Image.ALIGN_CENTER);
        document.add(pdfImage);
        document.close();

        showAlert("daijoubu", " pdf fairu saves shimashita:\n" + selectedFile.getAbsolutePath());
    }

    private void exportCurrentChartToHTML() {
        if (currentWebView == null) return;

        WebEngine webEngine = currentWebView.getEngine();
        Object result = webEngine.executeScript("document.documentElement.outerHTML");

        if (result instanceof String htmlContent) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save HTML File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("HTML Files", "*.html"));
            fileChooser.setInitialFileName("Dmagraph_" + currentStockCode + "_" + fromDt + "-" + toDt + ".html");
            File file = fileChooser.showSaveDialog(currentWebView.getScene().getWindow());

            if (file != null) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write(htmlContent);
                    showAlert("daijoobu", "HTML fairu save shimasthta:\n" + file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert("Error", "Failed to save HTML file.");
                }
            }
        } else {
            showAlert("Error", "Failed to extract HTML content.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
