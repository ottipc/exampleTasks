module com.devguy.example.texteditor {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.devguy.example.texteditor to javafx.fxml;
    exports com.devguy.example.texteditor;
}