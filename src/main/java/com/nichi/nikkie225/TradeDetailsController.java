package com.nichi.nikkie225;

import com.nichi.nikkie225.model.TradeEntry;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.List;

public class TradeDetailsController {

    @FXML private Label headerLabel;
    @FXML private TableView<TradeEntry> tradeDetailsTable;

    @FXML private TableColumn<TradeEntry, String> codeCol;
    @FXML private TableColumn<TradeEntry, Number> tradeNoCol;
    @FXML private TableColumn<TradeEntry, String> nameCol;
    @FXML private TableColumn<TradeEntry, Number> qtyCol;
    @FXML private TableColumn<TradeEntry, String> sideCol;
    @FXML private TableColumn<TradeEntry, String> dateCol;
    @FXML private TableColumn<TradeEntry, Number> priceCol;
    @FXML private TableColumn<TradeEntry, Number> isDeletedCol;

    public void setTradeDetails(String stockCode, List<TradeEntry> trades) {
        headerLabel.setText( stockCode);

        codeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCode()));
        tradeNoCol.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getTradeno()));
        nameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        qtyCol.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getQuantity()));
        sideCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSide()));
        dateCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTradedate()));
        priceCol.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getTradeprice()));
        isDeletedCol.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getIsdeleted()));

        tradeDetailsTable.setItems(FXCollections.observableArrayList(trades));
    }
}
