package Grava;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.jfree.chart.axis.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.statistics.Regression;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;

public class ScatterAdd extends Application {


    private final XYSeries series = new XYSeries("Voltage");
    private final XYSeries trend = new XYSeries("Trend");
    private final XYSeriesCollection dataset = new XYSeriesCollection(series);

    ChoiceBox<String> domainLabels = new ChoiceBox<>();
    ChoiceBox<String> rangeLabels = new ChoiceBox<>();

    private JFreeChart createChart() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        return ChartFactory.createScatterPlot("VI Characteristics", "Current", "Voltage", dataset);
    }

    @Override
    public void start(Stage stage) {

        Image image = new Image("Grava.logo.png");
        stage.getIcons().add(image);


        XYPlot plot = createChart().getXYPlot();
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        /*
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        adjustAxis((NumberAxis) plot.getDomainAxis(), true);
        adjustAxis((NumberAxis) plot.getRangeAxis(), false);
        */
        XYLineAndShapeRenderer r = (XYLineAndShapeRenderer) plot.getRenderer();
        r.setSeriesLinesVisible(1, Boolean.TRUE);
        r.setSeriesShapesVisible(1, Boolean.FALSE);

        var equation = new TextField();

        series.addChangeListener((event) -> {
            double[] coefficients = Regression.getOLSRegression(dataset, 0);
            double b = coefficients[0]; // intercept
            double m = coefficients[1]; // slope
            equation.setText("y = " + m + " x + " + b);

        });

        JFreeChart chart = createChart();
        domainLabels.getSelectionModel().selectedItemProperty().addListener((ov, s0, s1) -> {
            chart.getXYPlot().getDomainAxis().setLabel(s1);
        });
        rangeLabels.getSelectionModel().selectedItemProperty().addListener((ov, s0, s1) -> {
            chart.getXYPlot().getRangeAxis().setLabel(s1);
        });

        domainLabels.getItems().addAll("Current", "Seconds");
        domainLabels.setValue("Current");

        rangeLabels.getItems().addAll("Voltage", "Metres");
        rangeLabels.setValue("Voltage");

        var xSpin = new Spinner<Double>(-10000000.000, 10000000.000, 0, 0.1);
        xSpin.setEditable(true);
        xSpin.setPromptText("Xvalue");

        var ySpin = new Spinner<Double>(-10000000.000, 10000000.000, 0, 0.1);
        ySpin.setEditable(true);
        ySpin.setPromptText("Yvalue");

        var button = new Button("Add");
        button.setOnAction(ae -> series.add(xSpin.getValue(), ySpin.getValue()));


        HBox xBox = new HBox();
        xBox.getChildren().addAll(domainLabels);

        HBox yBox = new HBox();
        yBox.getChildren().addAll(rangeLabels);

        var enter = new ToolBar(xBox, xSpin, yBox, ySpin, button, equation);
        BorderPane.setAlignment(enter, Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setCenter(new ChartViewer(chart));
        root.setBottom(enter);

        stage.setTitle("ScatterAdd");
        stage.setScene(new Scene(root, 720, 480));
        stage.show();

    }
    /*
    private void adjustAxis(NumberAxis axis, boolean vertical) {
        axis.setRange(-3.0, 3.0);
        axis.setTickUnit(new NumberTickUnit(0.5));
        axis.setVerticalTickLabels(vertical);
    }
*/

    public static void main(String[] args) {
        launch(args);
    }
}