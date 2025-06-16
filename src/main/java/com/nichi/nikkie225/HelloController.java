package com.nichi.nikkie225;

import com.nichi.nikkie225.model.TradeEntry;
import com.nichi.nikkie225.model.Tradedto;
import com.nichi.nikkie225.model.dao.Pricedto;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class HelloController {

    @FXML private Label usernameLabel;
    @FXML private Button exportButton;
    @FXML private TableView<Tradedto> tableView;
    @FXML private TableColumn<Tradedto, String> stocksCol;
    @FXML private TableColumn<Tradedto, String> nameCol;
    @FXML private TableColumn<Tradedto, Double> positionTCol;
    @FXML private TableColumn<Tradedto, Double> positionTMinus1Col;
    @FXML private TableColumn<Tradedto, Double> tradePriceCol;
    @FXML private TableColumn<Tradedto, Double> cashflowCol;
    @FXML private TableColumn<Tradedto, Double> priceTCol;
    @FXML private TableColumn<Tradedto, Double> priceTMinus1Col;
    @FXML private TableColumn<Tradedto, Double> percentChangeCol;
    @FXML private TableColumn<Tradedto, Double> plCol;
    @FXML private TableColumn<Tradedto, Double> pricePLCol;
    @FXML private TableColumn<Tradedto, Double> tradePLCol;
    @FXML private TableColumn<Tradedto, Double> totalPLCol;
    @FXML private DatePicker tradeDatePicker;
    @FXML private Button loadButton;

    private final TradeService trade = new TradeService();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    @FXML
    public void initialize() {
        String username = System.getProperty("user.name");
        usernameLabel.setText("USERNAME : " + username);

        stocksCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getStocks()));
        nameCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getName()));
        positionTCol.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getPositionT()).asObject());
        positionTMinus1Col.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getPositionTm1()).asObject());
        tradePriceCol.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getTradePrice()).asObject());
        cashflowCol.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getCashflow()).asObject());
        priceTCol.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getPriceT()).asObject());
        priceTMinus1Col.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getPriceTm1()).asObject());
        percentChangeCol.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getPchange()).asObject());
        plCol.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getPL()).asObject());
        pricePLCol.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getPricePL()).asObject());
        tradePLCol.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getTradePL()).asObject());
        totalPLCol.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getTotalPL()).asObject());

        Callback<TableColumn<Tradedto, Double>, TableCell<Tradedto, Double>> formatter = column -> new TableCell<>() {
            private final DecimalFormat df = new DecimalFormat("#,##0.00");

            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? null : df.format(value));
            }
        };

        positionTCol.setCellFactory(formatter);
        positionTMinus1Col.setCellFactory(formatter);
        tradePriceCol.setCellFactory(formatter);
        cashflowCol.setCellFactory(formatter);
        priceTCol.setCellFactory(formatter);
        priceTMinus1Col.setCellFactory(formatter);
        percentChangeCol.setCellFactory(formatter);
        plCol.setCellFactory(formatter);
        pricePLCol.setCellFactory(formatter);
        tradePLCol.setCellFactory(formatter);
        totalPLCol.setCellFactory(formatter);

        tradeDatePicker.setValue(LocalDate.now());
        loadButton.setOnAction(e -> onLoadButtonClick());
        exportButton.setOnAction(e -> onExportButtonClick());

        addDoubleClickPopup(positionTCol, false);
        addDoubleClickPopup(positionTMinus1Col, true);

        tableView.getSelectionModel().setCellSelectionEnabled(true);
    }

    @FXML
    private void onLoadButtonClick() {
        LocalDate currentDate = tradeDatePicker.getValue();
        if (currentDate == null) {
            showAlert("Input Error", "Please select a trade date.");
            return;
        }

        String dateStr = currentDate.format(formatter);
        String dateTm1Str = currentDate.minusDays(1).format(formatter);

        List<TradeEntry> allTradesUpToT = trade.getDataTradeUntil(dateStr);
        List<TradeEntry> allTradesUpToTm1 = trade.getDataTradeUntil(dateTm1Str);
        List<Pricedto> priceList = trade.price();

        Map<String, TreeMap<LocalDate, Double>> priceMap = new HashMap<>();
        for (Pricedto p : priceList) {
            try {
                LocalDate d = LocalDate.parse(p.getDt(), formatter);
                priceMap.computeIfAbsent(p.getCode(), k -> new TreeMap<>()).put(d, p.getPrice());
            } catch (Exception ex) {
                System.err.println("Bad price date: " + p.getDt());
            }
        }

        Map<String, Integer> positionAtT = new HashMap<>();
        for (TradeEntry t : allTradesUpToT) {
            int sign = t.getSide().equalsIgnoreCase("b") ? +1 : -1;
            positionAtT.merge(t.getCode(), (int) (t.getQuantity() * sign), Integer::sum);
        }

        Map<String, Integer> positionAtTm1 = new HashMap<>();
        for (TradeEntry t : allTradesUpToTm1) {
            int sign = t.getSide().equalsIgnoreCase("b") ? +1 : -1;
            positionAtTm1.merge(t.getCode(), (int) (t.getQuantity() * sign), Integer::sum);
        }

        List<TradeEntry> tradesForTAndTm1 = new ArrayList<>();
        for (TradeEntry t : allTradesUpToT) {
            LocalDate tradeDate = LocalDate.parse(t.getTradedate(), formatter);
            if (tradeDate.equals(currentDate) || tradeDate.equals(currentDate.minusDays(1))) {
                tradesForTAndTm1.add(t);
            }
        }

        Map<String, List<TradeEntry>> tradesGrouped = tradesForTAndTm1.stream()
                .collect(Collectors.groupingBy(TradeEntry::getCode));

        Map<String, List<TradeEntry>> allTradesGrouped = allTradesUpToT.stream()
                .collect(Collectors.groupingBy(TradeEntry::getCode));

        List<Tradedto> rows = new ArrayList<>();
        for (var entry : tradesGrouped.entrySet()) {
            String code = entry.getKey();
            List<TradeEntry> trades = entry.getValue();
            String name = trades.get(0).getName();

            double netQty = 0.0;
            double cashflow = 0.0;
            double signedValueSum = 0.0;
            double signedQtySum = 0.0;

            for (TradeEntry t : trades) {
                double qty = t.getQuantity();
                double price = t.getTradeprice();

                int signQty = t.getSide().equalsIgnoreCase("b") ? +1 : -1;
                int signCash = t.getSide().equalsIgnoreCase("b") ? -1 : +1;

                netQty += signQty * qty;
                cashflow += signCash * (price * qty);
                signedValueSum += signQty * (price * qty);
                signedQtySum += signQty * qty;
            }

            double tradePrice = signedQtySum != 0 ? signedValueSum / signedQtySum : 0.0;

            double positionTVal = positionAtT.getOrDefault(code, 0);
            double positionTm1Val = positionAtTm1.getOrDefault(code, 0);

            TreeMap<LocalDate, Double> pricesForCode = priceMap.getOrDefault(code, new TreeMap<>());
            double priceT = pricesForCode.floorEntry(currentDate) != null ? pricesForCode.floorEntry(currentDate).getValue() : 0.0;
            double priceTm1 = pricesForCode.floorEntry(currentDate.minusDays(1)) != null ? pricesForCode.floorEntry(currentDate.minusDays(1)).getValue() : 0.0;
            double pctChange = priceTm1 != 0.0 ? ((priceT - priceTm1) * 100.0 / priceTm1) : 0.0;
            double pricePL = positionTVal * priceT - positionTm1Val * priceTm1;

            double totalCashflow = 0.0;
            List<TradeEntry> tradesAll = allTradesGrouped.getOrDefault(code, Collections.emptyList());
            for (TradeEntry t : tradesAll) {
                double qty = t.getQuantity();
                double price = t.getTradeprice();
                int signCash = t.getSide().equalsIgnoreCase("b") ? -1 : +1;
                totalCashflow += signCash * (price * qty);
            }

            double tradePL = totalCashflow;
            double TotalPL = tradePL + pricePL;
            double PL = pricePL + cashflow;

            Tradedto dto = new Tradedto(code, name, positionTVal, positionTm1Val,
                    tradePrice, cashflow, priceT, priceTm1, pctChange, PL, pricePL, tradePL, TotalPL);

            rows.add(dto);
        }

        tableView.setItems(FXCollections.observableArrayList(rows));
    }

    private void onExportButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialFileName("MointerTL_data");

        File file = fileChooser.showSaveDialog(tableView.getScene().getWindow());
        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("code,Name,PositionT,PositionT-1,TradePrice,Cashflow,PriceT,PriceT-1,%Change,PL,PricePL,TradePL,TotalPL");
                for (Tradedto dto : tableView.getItems()) {
                    writer.printf("%s,%s,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f%n",
                            dto.getStocks(), dto.getName(), dto.getPositionT(), dto.getPositionTm1(),
                            dto.getTradePrice(), dto.getCashflow(), dto.getPriceT(), dto.getPriceTm1(),
                            dto.getPchange(), dto.getPL(), dto.getPricePL(), dto.getTradePL(), dto.getTotalPL());
                }
                showAlert("Export Successful", "File saved: " + file.getAbsolutePath());
            } catch (Exception e) {
                showAlert("Export Error", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void addDoubleClickPopup(TableColumn<Tradedto, Double> column, boolean isTm1) {
        column.setCellFactory(col -> {
            TableCell<Tradedto, Double> cell = new TableCell<>() {
                private final DecimalFormat df = new DecimalFormat("#,##0.00");

                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : df.format(item));
                }
            };

            cell.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !cell.isEmpty()) {
                    Tradedto dto = tableView.getItems().get(cell.getIndex());
                    showtradelistdetails(dto.getStocks(), isTm1);
                }
            });

            return cell;
        });
    }


    private void showtradelistdetails(String stockCode, boolean isTm1) {
        try {
            LocalDate baseDate = tradeDatePicker.getValue();
            String formattedBaseDate = baseDate.format(formatter);

            List<TradeEntry> tradesUpToBase = trade.getDataTradeUntil(formattedBaseDate).stream()
                    .filter(t -> t.getCode().equalsIgnoreCase(stockCode))
                    .collect(Collectors.toList());

            if (tradesUpToBase.isEmpty()) {
                showAlert("No Trades", "No trades found for " + stockCode + " up to " + baseDate);
                return;
            }


            LocalDate targetDate;
            if (isTm1) {
                Optional<LocalDate> latestEarlierDateOpt = tradesUpToBase.stream()
                        .map(t -> LocalDate.parse(t.getTradedate(), formatter))
                        .filter(d -> d.isBefore(baseDate))
                        .max(Comparator.naturalOrder());

                if (latestEarlierDateOpt.isEmpty()) {
                    showAlert("trades illa", "munche yavadu illa trades " + stockCode  + baseDate);
                    return;
                }

                targetDate = latestEarlierDateOpt.get();
            } else {
                targetDate = baseDate;
            }


            List<TradeEntry> tradesOnTargetDate = tradesUpToBase.stream()
                    .filter(t -> LocalDate.parse(t.getTradedate(), formatter).equals(targetDate))
                    .collect(Collectors.toList());

            if (tradesOnTargetDate.isEmpty()) {
                showAlert("No Trades", "No trades found for " + stockCode + " on " + targetDate);
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("trade_details_popup.fxml"));
            Parent root = loader.load();
            TradeDetailsController controller = loader.getController();
            controller.setTradeDetails(stockCode, tradesOnTargetDate);

            Stage popupStage = new Stage();
            popupStage.setTitle("Trade Details: " + stockCode + " on " + targetDate);
            popupStage.setScene(new Scene(root));
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Popup Error", "Failed to show trade details.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
