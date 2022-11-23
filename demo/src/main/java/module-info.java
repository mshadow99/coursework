    module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.datatransfer;
    requires java.desktop;
        requires org.jfree.jfreechart;
        requires org.jfree.chart.fx;

        opens Grava to javafx.fxml;
    exports Grava;
}