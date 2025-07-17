package com.nichi.nikkie225;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.TreeSet;

public class DMAGraphController {

    @FXML private BorderPane rootPane;
    @FXML private TableView<PriceRiskFactor> dmaTable;
    @FXML private Pagination pagination;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> stockComboBox;

    private final int rowsPerPage = 10;
    private final ObservableList<PriceRiskFactor> fullData = FXCollections.observableArrayList();
    private final ObservableList<PriceRiskFactor> filteredData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadData();
        setupTable();
        updateTablePage(0);
        pagination.setPageFactory(this::createPage);
    }

    private void loadData() {
        fullData.clear();
        Set<String> uniqueStockCodes = new TreeSet<>();

        try (Connection conn = com.nichi.nikkie225.model.Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM priceriskfactor ORDER BY dt ASC")) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String code = rs.getString("code");
                int dt = rs.getInt("dt");
                String market = rs.getString("market");

                double dma200 = rs.getDouble("dma_200");
                double dma50 = rs.getDouble("dma_50");
                double dma13 = rs.getDouble("dma_13");
                double dma8 = rs.getDouble("dma_8");
                double dma5 = rs.getDouble("dma_5");

                fullData.add(new PriceRiskFactor(code, dt, market, dma200, dma50, dma13, dma8, dma5));
                uniqueStockCodes.add(code);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        stockComboBox.setItems(FXCollections.observableArrayList(uniqueStockCodes));
        filteredData.setAll(fullData);
        updatePagination();
    }

    private void updatePagination() {
        int pageCount = (int) Math.ceil(filteredData.size() / (double) rowsPerPage);
        pagination.setPageCount(Math.max(pageCount, 1));
        pagination.setCurrentPageIndex(0);
        updateTablePage(0);
    }

    private void setupTable() {
        dmaTable.getColumns().clear();

        dmaTable.getColumns().add(createColumn("CODE", "code"));
        dmaTable.getColumns().add(createColumn("DT", "dt"));
        dmaTable.getColumns().add(createColumn("MARKET", "market"));
        dmaTable.getColumns().add(createColumn("DMA200", "dma200"));
        dmaTable.getColumns().add(createColumn("DMA50", "dma50"));
        dmaTable.getColumns().add(createColumn("DMA13", "dma13"));
        dmaTable.getColumns().add(createColumn("DMA8", "dma8"));
        dmaTable.getColumns().add(createColumn("DMA5", "dma5"));

        TableColumn<PriceRiskFactor, Void> actionCol = new TableColumn<>("Graph");
        actionCol.setCellFactory(getLoadButtonCellFactory());
        dmaTable.getColumns().add(actionCol);
    }

    private <T> TableColumn<PriceRiskFactor, T> createColumn(String title, String property) {
        TableColumn<PriceRiskFactor, T> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        return col;
    }

    private Callback<TableColumn<PriceRiskFactor, Void>, TableCell<PriceRiskFactor, Void>> getLoadButtonCellFactory() {
        return col -> new TableCell<>() {
            private final Button btn = new Button("\uD83D\uDCC8");

            {
                btn.setStyle("-fx-font-size: 16px; -fx-background-color: transparent; -fx-cursor: hand;");
                btn.setTooltip(new Tooltip("View DMA Graph"));

                btn.setOnAction(event -> {
                    PriceRiskFactor data = getTableView().getItems().get(getIndex());
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("dma_graph_popup.fxml"));
                        BorderPane popupRoot = loader.load();

                        DMAGraphPopupController controller = loader.getController();


                        Integer selectedDt = null;
                        if (datePicker.getValue() != null) {
                            selectedDt = Integer.parseInt(datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                        }

                        controller.loadGraph(data.getCode(), selectedDt);

                        Stage popupStage = new Stage();
                        popupStage.setTitle("DMA for " + data.getCode());
                        popupStage.setScene(new Scene(popupRoot, 800, 500));
                        popupStage.initStyle(StageStyle.DECORATED);
                        popupStage.show();





            } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        };
    }

    private BorderPane createPage(int pageIndex) {
        updateTablePage(pageIndex);
        return new BorderPane(dmaTable);
    }

    private void updateTablePage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, filteredData.size());
        if (fromIndex > toIndex) {
            dmaTable.setItems(FXCollections.observableArrayList());
        } else {
            dmaTable.setItems(FXCollections.observableArrayList(filteredData.subList(fromIndex, toIndex)));
        }
    }

    @FXML
    private void onLoadDate() {
        LocalDate selectedDate = datePicker.getValue();
        String selectedStock = stockComboBox.getValue();

        filteredData.setAll(fullData.stream()
                .filter(p -> {
                     boolean dateMatches = (selectedDate == null || p.getDt() == Integer.parseInt(selectedDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"))));
                    boolean stockMatches = (selectedStock == null || selectedStock.isEmpty() || selectedStock.equals(p.getCode()));
                    return dateMatches && stockMatches;
                })
                .toList());

        updatePagination();
    }
}
