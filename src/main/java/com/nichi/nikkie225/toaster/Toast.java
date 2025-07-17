package com.nichi.nikkie225.toaster;



import com.nichi.nikkie225.model.TableTradeEntry;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Toast {
    public static void show(Node node, String message, int durationMillis) {
        Stage stage = (Stage) node.getScene().getWindow();
        Label toastLabel =  new Label(message);
        toastLabel.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-padding: 10px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        Popup popup = new Popup();
        popup.getContent().add(toastLabel);
        popup.setAutoHide(true);

        popup.show(stage);
        popup.setX(stage.getX() + (stage.getWidth() - toastLabel.getWidth()) / 2);
        popup.setY(stage.getY() + stage.getHeight() - 50);

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(durationMillis), ae -> popup.hide()));
        timeline.play();
    }
}
