module com.example.obiwankenobi {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.obiwankenobi to javafx.fxml;
    exports com.example.obiwankenobi;
}