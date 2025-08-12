module com.nichi.nikkie225 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;


    requires io.github.cdimascio.dotenv.java;
    requires java.naming;
    requires javafx.swing;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires jdk.compiler;
    requires org.apache.logging.log4j;
    requires jdk.jsobject;
    requires itextpdf;

    opens com.nichi.nikkie225.model to javafx.fxml;
    opens com.nichi.nikkie225 to javafx.fxml, org.hibernate.orm.core;
    opens com.nichi.nikkie225.model1 to org.hibernate.orm.core;

    exports com.nichi.nikkie225;
    exports com.nichi.nikkie225.model;
    exports com.nichi.nikkie225.model.dao;
    exports com.nichi.nikkie225.model1;
    exports com.nichi.nikkie225.dto;

}
