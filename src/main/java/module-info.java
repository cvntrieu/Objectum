module lma.objectum {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires jbcrypt;

    opens lma.objectum to javafx.fxml;
    exports lma.objectum;
    exports lma.objectum.Controllers;
    exports lma.objectum.Models;
    exports lma.objectum.Utils;
    opens lma.objectum.Controllers to javafx.fxml;
    opens lma.objectum.Models to javafx.fxml, javafx.base;
}