module com.example.obiwankenobi {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;
    requires jakarta.persistence;
    requires AnimateFX;
    requires java.desktop;

    opens com.example.obiwankenobi to javafx.fxml;
    exports com.example.obiwankenobi;
}