package com.nichi.nikkie225;

import com.nichi.nikkie225.model.TradeEntry;
import com.nichi.nikkie225.model.Tradedto;
import com.nichi.nikkie225.TradeService;
import com.nichi.nikkie225.model.dao.Pricedto;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;

public class HelloController {

    @FXML
    private TableView<Tradedto> tableView;
    @FXML
    private TableColumn<Tradedto,String> stocksCol;
    @FXML
    private TableColumn<Tradedto,String> nameCol;
    @FXML
    private TableColumn<Tradedto,Integer> positionTCol;
    @FXML
    private TableColumn<Tradedto,Integer> positionTMinus1Col;
    @FXML
    private TableColumn<Tradedto,Integer> tradePriceCol;
    @FXML
    private TableColumn<Tradedto,Integer> cashflowCol;
    @FXML
    private TableColumn<Tradedto,Integer> priceTCol;
    @FXML
    private TableColumn<Tradedto,Integer> priceTMinus1Col;
    @FXML
    private TableColumn<Tradedto,Double> percentChangeCol;
    @FXML
    private TableColumn<Tradedto,Integer> plCol;
    @FXML
    private TableColumn<Tradedto,Integer> pricePLCol;
    @FXML
    private TableColumn<Tradedto,Integer> tradePLCol;

    @FXML
    private DatePicker tradeDatePicker;

    @FXML
    private Button loadButton;

    @FXML
    public void initialize() {
        stocksCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStocks()));
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        positionTCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getPositionT()).asObject());
        positionTMinus1Col.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getPositionTm1()).asObject());
        tradePriceCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getTradePrice()).asObject());
        cashflowCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getCashflow()).asObject());
        priceTCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getPriceT()).asObject());
        priceTMinus1Col.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getPriceTm1()).asObject());
        percentChangeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getPchange()).asObject());
        plCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getPL()).asObject());
        pricePLCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getPricePL()).asObject());
        tradePLCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getTradePL()).asObject());

        tradeDatePicker.setValue(LocalDate.now());
    }

    TradeService trade = new TradeService();



    @FXML
    private void onLoadButtonClick() {
        List<TradeEntry> tabletrade = trade.getDataTrade();
        for (var v : tabletrade) {
            System.out.println(v);
        }
         List<Pricedto> price = trade.price();
        for(var v:price){
            System.out.println(v);
        }




    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
