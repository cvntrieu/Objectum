module lma.objectum {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;


    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.logging;
    requires java.sql;
    requires com.jfoenix;

    opens lma.objectum to javafx.fxml;
    exports lma.objectum;
    exports lma.objectum.Controllers;
    opens lma.objectum.Controllers to javafx.fxml;
}