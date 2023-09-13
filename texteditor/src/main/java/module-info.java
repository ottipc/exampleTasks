module com.devguy.texteditor {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.devguy.texteditor to javafx.fxml;
    exports com.devguy.texteditor;
}