module com.example.ejercicioexamendi {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jasperreports;
    requires java.desktop;


    opens com.example.ejercicioexamendi to javafx.fxml;
    exports com.example.ejercicioexamendi;
}