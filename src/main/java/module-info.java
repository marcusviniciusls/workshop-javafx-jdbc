module br.com.udemy.workshopjavafxjdbc {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;

    opens br.com.udemy.workshopjavafxjdbc to javafx.fxml;
    exports br.com.udemy.workshopjavafxjdbc;
    exports br.com.udemy.workshopjavafxjdbc.model.entities;
    opens br.com.udemy.workshopjavafxjdbc.model.entities to javafx.fxml;
    exports br.com.udemy.workshopjavafxjdbc.controller;
    opens br.com.udemy.workshopjavafxjdbc.controller to javafx.fxml;
}