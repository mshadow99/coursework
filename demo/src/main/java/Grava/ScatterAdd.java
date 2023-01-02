package Grava;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.statistics.Regression;;
import org.jfree.data.statistics.Statistics;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.util.List;

public class Grava extends Application {


    private static final XYSeries series = new XYSeries("Voltage");
    private final XYSeries trend = new XYSeries("Trend");
    private final XYSeriesCollection dataset = new XYSeriesCollection(series);

    ChoiceBox<String> domainLabels = new ChoiceBox<>();
    ChoiceBox<String> rangeLabels = new ChoiceBox<>();

    private JFreeChart createChart() {
        return ChartFactory.createScatterPlot("Grava", "Current", "Voltage", dataset);
    }

    @Override
    public void start(Stage stage) {

        Image image = new Image("Grava.logo.png");
        stage.getIcons().add(image);
        var chart = createChart();


        XYPlot plot = chart.getXYPlot();
        dataset.addSeries(trend);
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        XYLineAndShapeRenderer r = (XYLineAndShapeRenderer) plot.getRenderer();
        r.setSeriesLinesVisible(1, Boolean.TRUE);
        r.setSeriesShapesVisible(1, Boolean.FALSE);

        /*TableColumn xColumn = new TableColumn("X:");
        xColumn.setMinWidth(200);
        xColumn.setCellValueFactory(new PropertyValueFactory<>("X"));

        TableColumn yColumn = new TableColumn("Y:");
        xColumn.setMinWidth(200);
        xColumn.setCellValueFactory(new PropertyValueFactory<>("Y"));*/


        var equation = new TextField();

        series.addChangeListener((event) -> {
            if (series.getItemCount() > 1) {
                double[] coefficients = Regression.getOLSRegression(dataset, 0);
                double c = coefficients[0]; // intercept
                double m = coefficients[1]; // slope
                equation.setText("y = " + m + " x + " + c);
                double x = series.getDataItem(0).getXValue();
                trend.clear();
                trend.add(x, m * x + c);
                x = series.getDataItem(series.getItemCount() - 1).getXValue();
                trend.add(x, m * x + c);
                //equation.setText(String.valueOf(series.getDataItem(0).getXValue()));

            }
        });


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

        var addButton = new Button("Add");
        addButton.setOnAction(ae -> series.add(xSpin.getValue(), ySpin.getValue()));

        var g = chart.getXYPlot().getRenderer();
        var cb = new CheckBox("Trend");
        cb.setSelected(true);
        cb.selectedProperty().addListener((o) -> {
            if (series.getItemCount() > 1) {
                g.setSeriesVisible(1, cb.isSelected());
                /*double[] coefficients = Regression.getOLSRegression(dataset, 0);
                double b = coefficients[0]; // intercept
                double m = coefficients[1]; // slope
                double x = series.getDataItem(0).getXValue();
                trend.clear();
                trend.add(x, m * x + c);
                x = series.getDataItem(series.getItemCount() - 1).getXValue();
                trend.add(x, m * x + c);
                dataset.addSeries(trend);*/
            }
        });


        HBox xBox = new HBox();
        xBox.getChildren().addAll(domainLabels);

        HBox yBox = new HBox();
        yBox.getChildren().addAll(rangeLabels);

        var enter = new ToolBar(cb,xBox, xSpin, yBox, ySpin, addButton, equation);
        BorderPane.setAlignment(enter, Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setCenter(new ChartViewer(chart));
        root.setBottom(enter);

        stage.setTitle("ScatterAdd");
        stage.setScene(new Scene(root, 720, 480));
        stage.show();

    }
}
