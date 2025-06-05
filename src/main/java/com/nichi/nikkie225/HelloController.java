package com.nichi.nikkie225;

import com.nichi.nikkie225.model.TradeEntry;
import com.nichi.nikkie225.model.Tradedto;
import com.nichi.nikkie225.model.dao.Pricedto;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class HelloController {

    @FXML
    private Label usernameLabel;





    @FXML private TableView<Tradedto> tableView;
    @FXML private TableColumn<Tradedto, String> stocksCol;
    @FXML private TableColumn<Tradedto, String> nameCol;
    @FXML private TableColumn<Tradedto, Integer> positionTCol;
    @FXML private TableColumn<Tradedto, Integer> positionTMinus1Col;
    @FXML private TableColumn<Tradedto, Double> tradePriceCol;
    @FXML private TableColumn<Tradedto, Integer> cashflowCol;
    @FXML private TableColumn<Tradedto, Integer> priceTCol;
    @FXML private TableColumn<Tradedto, Integer> priceTMinus1Col;
    @FXML private TableColumn<Tradedto, Double> percentChangeCol;
    @FXML private TableColumn<Tradedto, Integer> plCol;
    @FXML private TableColumn<Tradedto, Integer> pricePLCol;
    @FXML private TableColumn<Tradedto, Integer> tradePLCol;

    @FXML private DatePicker tradeDatePicker;
    @FXML private Button loadButton;

    private final TradeService trade = new TradeService();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");


    @FXML
    public void initialize() {

        String username=System.getProperty("user.name");
        usernameLabel.setText("USERNAME"+" : "+username);


        stocksCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getStocks()));
        nameCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getName()));
        positionTCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getPositionT()).asObject());
        positionTMinus1Col.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getPositionTm1()).asObject());
        tradePriceCol.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getTradePrice()).asObject());
        cashflowCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getCashflow()).asObject());
        priceTCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getPriceT()).asObject());
        priceTMinus1Col.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getPriceTm1()).asObject());
        percentChangeCol.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getPchange()).asObject());
        plCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getPL()).asObject());
        pricePLCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getPricePL()).asObject());
        tradePLCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getTradePL()).asObject());

        tradeDatePicker.setValue(LocalDate.now());
        loadButton.setOnAction(e -> onLoadButtonClick());
    }

    @FXML
    private void onLoadButtonClick() {
        LocalDate currentDate = tradeDatePicker.getValue();
        if (currentDate == null) {
            showAlert("Input Error", "Please select a trade date.");
            return;
        }

        List<TradeEntry> allTrades = trade.getDataTradeUntil(java.sql.Date.valueOf(currentDate));
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

        Map<String, Integer> tradePLMap = new HashMap<>();
        for (TradeEntry t : allTrades) {
            int signTradePL = t.getSide().equalsIgnoreCase("b") ? -1 : +1;
            tradePLMap.merge(
                    t.getCode(),
                    t.getTradeprice() * t.getQuantity() * signTradePL,
                    Integer::sum
            );
        }

        LocalDate prevDate = currentDate.minusDays(1);
        List<TradeEntry> before = trade.getDataTradeUntil(java.sql.Date.valueOf(prevDate));
        Map<String, Integer> posTm1 = new HashMap<>();
        for (TradeEntry t : before) {
            int sign = t.getSide().equalsIgnoreCase("b") ? +1 : -1;
            posTm1.merge(t.getCode(), t.getQuantity() * sign, Integer::sum);
        }

        Map<String, List<TradeEntry>> todayMap = allTrades.stream()
                .filter(t -> {
                    try {
                        return LocalDate.parse(t.getTradedate(), formatter).equals(currentDate);
                    } catch (Exception ex) {
                        return false;
                    }
                })
                .collect(Collectors.groupingBy(TradeEntry::getCode));

        List<Tradedto> rows = new ArrayList<>();
        for (var en : todayMap.entrySet()) {
            String code = en.getKey();
            List<TradeEntry> trades = en.getValue();
            String name = trades.get(0).getName();

            int netQty = 0, cashflow = 0;
            double sumSignedValue = 0, sumSignedQty = 0;

            for (TradeEntry t : trades) {
                int qty = t.getQuantity();
                int price = t.getTradeprice();
                int signQty = t.getSide().equalsIgnoreCase("b") ? +1 : -1;   // for position
                int signCash = t.getSide().equalsIgnoreCase("b") ? -1 : +1;  // for cashflow

                netQty += signQty * qty;
                cashflow += signCash * price * qty;

                sumSignedValue += signQty * price * qty;
                sumSignedQty += signQty * qty;
            }

            double rawTP = sumSignedQty != 0 ? sumSignedValue / sumSignedQty : 0.0;
            double tradePrice = Math.round(rawTP * 100.0) / 100.0;

            int positionTm1 = posTm1.getOrDefault(code, 0);

            var m = priceMap.getOrDefault(code, new TreeMap<>());
            var eT = m.floorEntry(currentDate);
            var eM1 = eT != null ? m.lowerEntry(eT.getKey()) : null;
            double priceT = eT != null ? eT.getValue() : 0.0;
            double priceTm1 = eM1 != null ? eM1.getValue() : 0.0;

            double rawPct = priceTm1 != 0.0 ? (priceT - priceTm1) * 100.0 / priceTm1 : 0.0;
            double pct = Math.round(rawPct * 100.0) / 100.0;

            int pricePL = (int) Math.round(netQty * priceT - positionTm1 * priceTm1);
            int PL = pricePL + cashflow;
            int tradePL = cashflow;

            Tradedto dto = new Tradedto(
                    code, name,
                    netQty, positionTm1,
                    tradePrice, cashflow,
                    (int) priceT, (int) priceTm1,
                    pct, PL, pricePL, tradePL
            );
            System.out.println(dto);
            rows.add(dto);
        }

        tableView.setItems(FXCollections.observableArrayList(rows));
    }

    private void showAlert(String title, String message) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle(title);
        a.setContentText(message);
        a.showAndWait();
    }
}
