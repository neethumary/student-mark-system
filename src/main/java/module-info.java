module com.example.sms {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.swing;   // Needed for SwingFXUtils
    requires java.desktop; 
    requires com.github.librepdf.openpdf; // OpenPDF library
    requires java.sql;

    opens com.example.sms to javafx.fxml;
    exports com.example.sms;
}
