module lma.objectum {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires jbcrypt;
    requires com.google.gson; // Thêm thư viện Gson
    requires okhttp3;
    requires java.desktop;         // Thêm thư viện OkHttp

    opens lma.objectum to javafx.fxml;
    exports lma.objectum;
    exports lma.objectum.Controllers;
    opens lma.objectum.Controllers to javafx.fxml;

    // Mở gói Models cho cả javafx.fxml và javafx.base
    opens lma.objectum.Models to javafx.fxml, javafx.base;
}
