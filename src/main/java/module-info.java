module com.nichi.nikkie225 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    
    requires com.almasb.fxgl.all;
    requires io.github.cdimascio.dotenv.java;
    requires java.sql;
    requires java.desktop;

    opens com.nichi.nikkie225 to javafx.fxml;
    exports com.nichi.nikkie225;
    exports com.nichi.nikkie225.model;
    opens com.nichi.nikkie225.model to javafx.fxml;
}