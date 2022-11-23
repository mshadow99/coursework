package Grava;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.statistics.Regression;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class ScatterAdd extends Application {


    private final XYSeries series = new XYSeries("Voltage");
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


        ChoiceBox<String> domainLabels = new ChoiceBox<>();
        ChoiceBox<String> rangeLabels = new ChoiceBox<>();

        XYPlot plot = createChart().getXYPlot();
        XYLineAndShapeRenderer r = (XYLineAndShapeRenderer) plot.getRenderer();
        r.setSeriesLinesVisible(1, Boolean.TRUE);
        r.setSeriesShapesVisible(1, Boolean.FALSE);

        series.addChangeListener((event) -> {
                    double[] coefficients = Regression.getOLSRegression(dataset, 0);
                    double b = coefficients[0]; // intercept
                    double m = coefficients[1]; // slope
                    System.out.println("y = " + m + " x + " + b);

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


        var xSpin = new Spinner<Double>(-10000000.000, 10000000.000, 0,0.1);
        xSpin.setEditable(true);
        xSpin.setPromptText("Xvalue");

        var ySpin = new Spinner<Double>(-10000000.000, 10000000.000, 0,0.1);
        ySpin.setEditable(true);
        ySpin.setPromptText("Yvalue");

        var button = new Button("Add");
        button.setOnAction(ae -> series.add(xSpin.getValue(), ySpin.getValue()));


        HBox xBox = new HBox();
        xBox.getChildren().addAll(domainLabels);

        HBox yBox = new HBox();
        yBox.getChildren().addAll(rangeLabels);

        var enter = new ToolBar(xBox, xSpin, yBox, ySpin, button);
        BorderPane.setAlignment(enter, Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setCenter(new ChartViewer(chart));
        root.setBottom(enter);

        stage.setTitle("ScatterAdd");
        stage.setScene(new Scene(root, 640, 480));
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}