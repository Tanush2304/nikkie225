package com.nichi.nikkie225;

import com.nichi.nikkie225.dto.MinMaxBound;
import com.nichi.nikkie225.dto.TradeEntryDTO;
import com.nichi.nikkie225.model.TableTradeEntry;
import com.nichi.nikkie225.model.dao.TradeEntryHelper;
import com.nichi.nikkie225.model1.TradeList;
//import com.nichi.nikkie225.toaster.Toast;

import com.nichi.nikkie225.toaster.Toast;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import com.nichi.nikkie225.model.dao.StockListDAO;
import com.nichi.nikkie225.model1.StocksList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


public class TradeEntryController {

    private static final Logger logging = LogManager.getLogger(TradeEntryController.class);

    @FXML
    private TableView<TableTradeEntry> tableTradeEntry;
    @FXML
    private Button saveBtn;

    @FXML
    private TableColumn<TableTradeEntry, Integer> colTradeNo;
    @FXML
    private TableColumn<TableTradeEntry, String> colCode;
    @FXML
    private TableColumn<TableTradeEntry, String> colName;
    @FXML
    private TableColumn<TableTradeEntry, String> colTradeDate;
    @FXML
    private TableColumn<TableTradeEntry, String> colSide;
    @FXML
    private TableColumn<TableTradeEntry, Double> colTradePrice;
    @FXML
    private TableColumn<TableTradeEntry, Integer> colQuantity;
    @FXML
    private TableColumn<TableTradeEntry, Boolean> colCheck;

    @FXML
    private ComboBox<String> filterComboBox;

    @FXML
    private CheckBox myCheckBox;

    @FXML
    private Button exportbtn;



    private final ObservableList<TableTradeEntry> tradeData = FXCollections.observableArrayList();
    private final FilteredList<TableTradeEntry> filteredData = new FilteredList<>(tradeData, p -> true);
    MinMaxBound bounds;
    String selectedCopy;
    Integer tradeNo;


    @FXML
    public void initialize() throws Exception {
        OnClickLoad();
        tableTradeEntry.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableTradeEntry.setItems(filteredData);
        tableTradeEntry.setEditable(true);



        StockListDAO stockListDAO = new StockListDAO();
        List<StocksList> stocksLists = stockListDAO.getAllStockList();
        ObservableList<String> stocksCode = FXCollections.observableArrayList();
        TradeEntryDAO tradeEntryDAO = new TradeEntryDAO();
        for (var stocks : stocksLists) {
            stocksCode.add(stocks.getCodeId());
        }

        Map<String, String> codeToName = stocksLists.stream()
                .collect(Collectors.toMap(StocksList::getCodeId, StocksList::getName));
        String getValue = "";

        filterComboBox.setItems(stocksCode);
        filterComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(entry -> {
                if (newVal == null || newVal.isEmpty() || newVal.equals("All")) {
                    return true;
                }
                tableTradeEntry.setPlaceholder(new Label("No data available for code: " + newVal));
                return entry.getCode().equals(newVal);
            });
        });

        colTradeNo.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getTradeNo()).asObject());
        colTradeNo.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colTradeNo.setOnEditCommit(event -> event.getRowValue().setTradeNo(event.getNewValue()));

        colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
        colCode.setCellFactory(column -> {
            ComboBoxTableCell<TableTradeEntry, String> cell =
                    new ComboBoxTableCell<>(stocksCode);
            cell.itemProperty().addListener((obs, oldValue, newValue) -> {
                try {
                    TableTradeEntry trade = cell.getTableRow().getItem();
                    if (trade != null && trade.isModifiedTradeCode()) {
                        cell.setStyle("-fx-background-color: #efde91; -fx-border-color: yellow; -dx-text-fill: black;");
                    }else {
                        cell.setStyle("");
                    }
                }catch (Exception e){}
            });
            return cell;
        });
        colCode.setOnEditCommit(event -> {
            TableTradeEntry row = event.getRowValue();
            String oldRow = event.getOldValue();
            String selectedRow = event.getNewValue();


            if (!oldRow.equals(selectedRow) && !oldRow.isEmpty()) {
                row.setCode(selectedRow);
                row.setModifiedTradeCode(true);
                String name = codeToName.getOrDefault(selectedRow, "");
                Tooltip tooltip = new Tooltip("Hello");

                row.setName(name);
                tableTradeEntry.refresh();
            } else if (oldRow.isEmpty()) {
                row.setCode(selectedRow);
                String name = codeToName.getOrDefault(selectedRow, "");
                row.setName(name);
                tableTradeEntry.refresh();
            }
            selectedCopy = selectedRow;
            System.out.println(selectedCopy);
            bounds = tradeEntryDAO.getMinMaxValue(selectedRow, "20250611");
            Toast.show(tableTradeEntry, "The min and max value of " + selectedRow + " is " + bounds.getLowerBond()  + " to " + bounds.getHigherBond(), 2000);
        });

        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        colName.setCellFactory(column -> {
            TextFieldTableCell<TableTradeEntry, String> cell =
                    new TextFieldTableCell<>();
            cell.itemProperty().addListener((cols, oldValue, newValue) -> {
                try {
                    TableTradeEntry trade = cell.getTableRow().getItem();
                    if (trade != null && trade.isModifiedTradeCode()) {
                        cell.setStyle("-fx-background-color: #efde91; -fx-border-color: yellow; -dx-text-fill: black;");
                    } else {
                        cell.setStyle("");
                    }
                }catch (Exception e) {}
            });
            return cell;
        });
        colName.setOnEditCommit(event -> event.getRowValue().setName(event.getNewValue()));

        colTradeDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTradeDate()));
        colTradeDate.setOnEditCommit(event -> event.getRowValue().setTradeDate(event.getNewValue()));

        colSide.setEditable(true);
        colSide.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSide()));
        colSide.setCellFactory(column -> {
            ComboBoxTableCell<TableTradeEntry, String> cell =
                    new ComboBoxTableCell<>("B", "S");
            cell.itemProperty().addListener((obs, oldValue, newValue) -> {
                try {
                    TableTradeEntry trade = cell.getTableRow().getItem();
                    if (trade != null && trade.isModifiedSide()) {
                        cell.setStyle("-fx-background-color: #efde91; -fx-border-color: yellow; -dx-text-fill: black;");
                    }else {
                        cell.setStyle("");
                    }
                }catch (Exception e){}
            });
            return cell;
        });
        colSide.setOnEditCommit(event -> {
            TableTradeEntry trade = event.getRowValue();
            String oldValue = event.getOldValue();
            String newValue = event.getNewValue();

            if (!oldValue.equals(newValue) && !oldValue.isEmpty()) {
                trade.setSide(newValue);
                trade.setModifiedSide(true);
                tableTradeEntry.refresh();
            } else if (oldValue.isEmpty()) {
                trade.setSide(newValue);
                tableTradeEntry.refresh();
            }
        });

        colTradePrice.setEditable(true);
        colTradePrice.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getTradePrice()).asObject());
        colTradePrice.setCellFactory(column -> {
            TextFieldTableCell<TableTradeEntry, Double> cell =
                    new TextFieldTableCell<>(new DoubleStringConverter());
            cell.itemProperty().addListener((obs, oldItem, newItem) -> {
                try {
                    TableTradeEntry trade = cell.getTableRow().getItem();
                    if (bounds != null && selectedCopy != null) {
                        Tooltip tooltip = new Tooltip(bounds.getLowerBond() + " - " + bounds.getHigherBond());
                        cell.setTooltip(tooltip);
                    }
                    System.out.println(trade.getTradeNo() + " " +trade.isMisMatch());
                    if (trade.isMisMatch()) {
                        cell.setStyle("-fx-background-color: #ffdfd5; -fx-text-fill: black; -fx-border-color: red;");
                    } else if (trade.isMatch()) {
                        cell.setStyle("-fx-background-color: #55DD33; -fx-text-fill: black; -fx-border-color: green;");
                    } else if (trade != null && trade.isModifiedTradePrice()) {
                        cell.setStyle("-fx-background-color: #efde91; -fx-border-color: yellow; -dx-text-fill: black;");
                    }else {
                        cell.setStyle("");
                    }
                }catch (Exception e) {
                }
            });
            return cell;
        });
        colTradePrice.setOnEditCommit(event -> {
            TableTradeEntry trade = event.getRowValue();
            Double oldValue = event.getOldValue();
            Double newValue = Math.round(event.getNewValue() * 100.0) / 100.0;


            if (!oldValue.equals(newValue) && oldValue != 0.0) {
                trade.setTradePrice(newValue);
                trade.setModifiedTradePrice(true);
                tableTradeEntry.refresh();
            } else if (oldValue == 0.0) {
                trade.setTradePrice(newValue);
                tableTradeEntry.refresh();
            }
            if (newValue < bounds.getLowerBond() || newValue > bounds.getHigherBond()) {
                trade.setMisMatch(true);
                trade.setMatch(false);
                tableTradeEntry.refresh();
            }else if (newValue > bounds.getLowerBond() || newValue < bounds.getHigherBond()) {
                trade.setMisMatch(false);
                trade.setMatch(true);
                tableTradeEntry.refresh();
            }
        });

        colQuantity.setEditable(true);
        colQuantity.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantity()).asObject());
        colQuantity.setCellFactory(column -> {
            TextFieldTableCell<TableTradeEntry, Integer> cell =
                    new TextFieldTableCell<>(new IntegerStringConverter());

            cell.itemProperty().addListener((obs, oldItem, newItem) -> {
                try {
                    TableTradeEntry trade = cell.getTableRow().getItem();
                    if (trade != null && trade.isModifiedQuantity()) {
                        cell.setStyle("-fx-background-color: #efde91; -fx-border-color: yellow; -dx-text-fill: black;");
                    }else {
                        cell.setStyle("");
                    }
                }catch (Exception e) {
                }

            });
            return cell;
        });
        colQuantity.setOnEditCommit(event -> {
            TableTradeEntry trade = event.getRowValue();
            Integer oldValue = event.getOldValue();
            Integer newValue = event.getNewValue();

            if (!oldValue.equals(newValue) && oldValue != 0) {
                trade.setQuantity(newValue);
                trade.setModifiedQuantity(true);
                tableTradeEntry.refresh();
            }else if (oldValue == 0) {
                trade.setQuantity(newValue);
                tableTradeEntry.refresh();
            }
        });

        tableTradeEntry.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.TAB) {
                TablePosition<?, ?> pos = tableTradeEntry.getFocusModel().getFocusedCell();
                if (pos != null) {
                    int colIndex = pos.getColumn();
                    int rowIndex = pos.getRow();
                    int totalCols = tableTradeEntry.getColumns().size();

                    int nextColIndex = (colIndex + 1) % totalCols;

                    tableTradeEntry.edit(rowIndex, tableTradeEntry.getColumns().get(nextColIndex));


                    Platform.runLater(() -> {
                        tableTradeEntry.getSelectionModel().select(rowIndex);
                        tableTradeEntry.getFocusModel().focus(rowIndex, tableTradeEntry.getColumns().get(nextColIndex));
                        tableTradeEntry.edit(rowIndex, tableTradeEntry.getColumns().get(nextColIndex));
                    });
                    event.consume();
                }
            }
        });

        ContextMenu contextMenu = new ContextMenu();

        MenuItem addItem = new MenuItem("Add");
        addItem.setOnAction(e -> {
            int maxTradeNo = tradeData.stream()
                    .mapToInt(TableTradeEntry::getTradeNo)
                    .max()
                    .orElse(0);
            int nextTradeNo = maxTradeNo + 1;
            tradeNo = nextTradeNo;

            TableTradeEntry newEntry = new TableTradeEntry(nextTradeNo, "", "", "", "", 0.0, 0, false);
            tradeData.add(newEntry);
            int newIndex = tradeData.size() - 1;
            tableTradeEntry.scrollTo(newIndex);
            tableTradeEntry.getSelectionModel().select(newIndex);
            tableTradeEntry.layout();
            tableTradeEntry.edit(newIndex, colTradeNo);

        });

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(e -> {
            TableTradeEntry selected = tableTradeEntry.getSelectionModel().getSelectedItem();
            if (selected != null) {
                tradeData.remove(selected);
            }
        });

        contextMenu.getItems().addAll(addItem, deleteItem);

        tableTradeEntry.setContextMenu(contextMenu);

        tableTradeEntry.setRowFactory(tv -> {
            TableRow<TableTradeEntry> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    tableTradeEntry.getSelectionModel().select(row.getIndex());
                }
            });
            return row;
        });

        deleteItem.setOnAction(e -> {
            TableTradeEntry selected = tableTradeEntry.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Deleted Selected row?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES){
                        tradeEntryDAO.deleteTrade(selected.getTradeNo(), selected.getCode());
                        tradeData.remove(selected);
                    }
                });
            }
        });

        colTradeDate.setCellFactory(column -> new TableCell<TableTradeEntry, String>() {
            private final DatePicker datePicker = new DatePicker();
            {
                datePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(DatePicker param) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item.isAfter(LocalDate.now())) {
                                    setDisable(true);
                                    setCursor(Cursor.DISAPPEAR);
                                }
                            }
                        };
                    }
                });
                datePicker.setOnAction(event -> {
                    int row = getIndex();
                    TableTradeEntry item = getTableView().getItems().get(row);
                    String newDate = "";
                    try {
                        newDate = datePicker.getValue().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
                    }catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                    item.setTradeDate(newDate);
                    commitEdit(newDate);
                });
                setGraphic(datePicker);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                }else {
                    try {
                        datePicker.setValue(LocalDate.parse(item, java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd")));
                    }catch (Exception e) {
                        datePicker.setValue(null);
                    }

                    TableTradeEntry trade = getTableView().getItems().get(getIndex());
                    setGraphic(datePicker);
                }
            }
            @Override
            public void startEdit() {
                super.startEdit();
                datePicker.requestFocus();
            }
        });

        colCheck.setCellValueFactory(cellData -> cellData.getValue().isDeletedProperty());
        colCheck.setCellFactory(CheckBoxTableCell.forTableColumn(colCheck));

    }


    @FXML
    public void OnClickSave(ActionEvent event) {

        for (TableTradeEntry entry : tradeData) {
            if (!isValid(entry)){
                return;
            }
        }
        Optional<TableTradeEntry> result = tradeData.stream()
                .filter(b -> Objects.equals(b.getCode(), selectedCopy) && Objects.equals(b.getTradeNo(), tradeNo))
                .findFirst();

        if (result.isPresent()) {
            if (!isValids(result.get())){
                return;
            }
        }

        List<TradeList> trade = tradeData.stream()
                .map(entry -> new TradeList(
                        entry.getTradeNo(),
                        entry.getCode(),
                        entry.getName(),
                        entry.getTradeDate(),
                        entry.getSide(),
                        entry.getTradePrice(),
                        entry.getQuantity(),
                        "tradeentrytool",
                        getDateTime()
                ))
                .toList();

        TradeEntryDAO tradeEntryDAO = new TradeEntryDAO();
        tradeEntryDAO.saveAll(trade);

        List<TradeEntryHelper> selected = tradeData.stream()
                .filter(TableTradeEntry::isIsDeleted)
                .map(p -> new TradeEntryHelper(p.getTradeNo(), p.getCode()))
                .toList();

        tradeEntryDAO.deleteTrade(selected);

        System.out.println(selected);
        myCheckBox.setText("unHide");
        myCheckBox.setSelected(false);

        OnClickLoad();
    }

    @FXML
    public void OnClickLoad() {
        TradeEntryDAO tradeEntryDAO = new TradeEntryDAO();
        List<TradeList> tradeLists = tradeEntryDAO.getAllTradeList();

        tradeData.clear();

        for (TradeList trade : tradeLists) {
            TableTradeEntry entry = new TableTradeEntry(
                    trade.getTradeNo(),
                    trade.getCode(),
                    trade.getName(),
                    trade.getTradeDate(),
                    trade.getSide(),
                    trade.getTradePrice(),
                    trade.getQuantity(),
                    false
            );
            tradeData.add(entry);
        }
        myCheckBox.setText("unHide");
        myCheckBox.setSelected(false);
        filterComboBox.setValue("All");

        tradeEntryDAO.getMinMaxValue("AXISBANK", "20250611");

        tableTradeEntry.refresh();
    }

    @FXML
    public void onClickHideAndUnhide(ActionEvent event) {
        TradeEntryDAO tradeEntryDAO = new TradeEntryDAO();
        if (myCheckBox.isSelected()) {
            List<TradeList> tradeLists = tradeEntryDAO.getAllTrades();
            tradeData.clear();
            for (var trade : tradeLists) {
                if (trade.getIsDeleted() == 1) {
                    TableTradeEntry tradeEntry = new TableTradeEntry(
                            trade.getTradeNo(),
                            trade.getCode(),
                            trade.getName(),
                            trade.getTradeDate(),
                            trade.getSide(),
                            trade.getTradePrice(),
                            trade.getQuantity(),
                            true
                    );
                    tradeData.add(tradeEntry);
                } else if (trade.getIsDeleted() == 0) {
                    TableTradeEntry tradeEntry = new TableTradeEntry(
                            trade.getTradeNo(),
                            trade.getCode(),
                            trade.getName(),
                            trade.getTradeDate(),
                            trade.getSide(),
                            trade.getTradePrice(),
                            trade.getQuantity(),
                            false
                    );
                    tradeData.add(tradeEntry);

                }
            }
            myCheckBox.setText("hide");
        } else {
            List<TradeList> tradeLists = tradeEntryDAO.getAllTradeList();
            tradeData.clear();
            for (var trade : tradeLists) {
                TableTradeEntry tradeEntry = new TableTradeEntry(
                        trade.getTradeNo(),
                        trade.getCode(),
                        trade.getName(),
                        trade.getTradeDate(),
                        trade.getSide(),
                        trade.getTradePrice(),
                        trade.getQuantity(),
                        false
                );
                tradeData.add(tradeEntry);
            }
            myCheckBox.setText("unHide");
        }
    }

    @FXML
    protected void OnExport() {


        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        fileChooser.setInitialFileName("stocks_" + getDateTime().substring(0,9) + ".csv");



        if (myCheckBox.isSelected()) {
            fileChooser.setInitialFileName("all_stocks_" + getDateTime().substring(0,9) + ".csv");
            File file = fileChooser.showSaveDialog(exportbtn.getScene().getWindow());
            if (file != null) {
                exportCSV(file);
            }
        }else {
            fileChooser.setInitialFileName("stocks_" + getDateTime().substring(0,9) + ".csv");
            File file = fileChooser.showSaveDialog(exportbtn.getScene().getWindow());
            if (file != null) {
                exportCSV(file);
            }
        }

    }

    private void exportCSV(File file) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {

            bw.write("Trade No,Code,Name,TradeDate,Side,TradePrice,Quantity,(0) Not Deleted (1) Deleted");
            bw.newLine();

            List<TradeEntryDTO> csvData = tradeData.stream()
                    .map(trade -> new TradeEntryDTO(
                            trade.getTradeNo(),
                            trade.getCode(),
                            trade.getName(),
                            trade.getTradeDate(),
                            trade.getSide(),
                            trade.getTradePrice(),
                            trade.getQuantity(),
                            trade.isIsDeleted() ? 1 : 0
                    )).toList();

            for (TradeEntryDTO data : csvData) {
                String line = String.join(",",
                        String.valueOf(data.getTradeNo()),
                        data.getCode(),
                        data.getName(),
                        data.getTradeDate(),
                        data.getSide(),
                        String.valueOf(data.getTradePrice()),
                        String.valueOf(data.getQuantity()),
                        String.valueOf(data.getIsDeleted())
                );
                bw.write(line);
                bw.newLine();
            }

            showAlert("CSV Exported Successfully: " + file.getAbsolutePath());

        } catch (IOException e) {
            showAlert("Error exporting CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        return dateFormat.format(new Date()).toLowerCase();
    }

    private boolean isValid (TableTradeEntry entry) {
        StringBuilder error = new StringBuilder();

        if (entry.getCode() == null || entry.getCode().isEmpty()) {
            error.append("Code : code is required! \n");
        }
        if (entry.getSide() == null || (!entry.getSide().equals("B") && !entry.getSide().equals("S"))) {
            error.append("Side : Side must be B or S \n");
        }
        if (entry.getTradeDate() == null || entry.getTradeDate().isEmpty()) {
            error.append("Date : Date should be selected \n");
        }
        if (entry.getTradePrice() <= 0) {
            error.append("Trade Price : Enter a valid Trade price \n");
        }
//        if (entry.getQuantity() <= 0) {
//            error.append("Trade Quantity : Enter a valid Quantity \n");
//        }
        if (!error.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Invalid Trade entry: Trade no: " + entry.getTradeNo());
            alert.setContentText(error.toString());
            alert.showAndWait();
            return false;
        }
        return true;
    }

    private boolean isValids(TableTradeEntry comp) {
        try {
            if (comp.getTradePrice() < bounds.getLowerBond() || comp.getTradePrice() > bounds.getHigherBond()) {
                showAlert("The min and max value of " + selectedCopy + "should be " + bounds.getLowerBond() + " to " + bounds.getHigherBond());
                return false;
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());

        }
        return true;
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Export Status");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}