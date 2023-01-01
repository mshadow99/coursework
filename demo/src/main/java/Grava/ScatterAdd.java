package Grava;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
import org.jfree.data.statistics.Regression;;
import org.jfree.data.statistics.Statistics;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.util.List;

public class ScatterAdd extends Application {


    private static final XYSeries series = new XYSeries("Voltage");
    private final XYSeries trend = new XYSeries("Trend");
    private final XYSeriesCollection dataset = new XYSeriesCollection(series);
    TableView table;

    ChoiceBox<String> domainLabels = new ChoiceBox<>();
    ChoiceBox<String> rangeLabels = new ChoiceBox<>();

    private JFreeChart createChart() {
        return ChartFactory.createScatterPlot("VI Characteristics", "Current", "Voltage", dataset);
    }

    @Override
    public void start(Stage stage) {

        Image image = new Image("Grava.logo.png");
        stage.getIcons().add(image);
        var chart = createChart();


        XYPlot plot = chart.getXYPlot();
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        XYLineAndShapeRenderer r = (XYLineAndShapeRenderer) plot.getRenderer();
        r.setSeriesLinesVisible(1, Boolean.TRUE);
        r.setSeriesShapesVisible(1, Boolean.FALSE);


        var equation = new TextField();
        var rValuebox = new TextField();
        var rBox = new TextField();
        series.addChangeListener((event) -> {
            if (series.getItemCount() > 1) {
                double[] coefficients = Regression.getOLSRegression(dataset, 0);
                double b = coefficients[0]; // intercept
                double m = coefficients[1]; // slope
                equation.setText("y = " + m + " x + " + b);
                double x = series.getDataItem(0).getXValue();
                /*trend.clear();
                trend.add(x, m * x + b);
                x = series.getDataItem(series.getItemCount() - 1).getXValue();
                trend.add(x, m * x + b);
                dataset.addSeries(trend);*/
                equation.setText(String.valueOf(series.getDataItem(0).getXValue()));

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

        Number[] xList = {1,2,3,4,5,6};
        Number[] yList = {2,4,7,9,12,14};

        System.out.println(xList[1]);
        List arr = series.getItems();

        double[][] arr1 = series.toArray();

        int i;
        float sum;
        for (i = 0; i < (series.getItemCount() - 1); i++) {
            sum = (int) series.getDataItem(i).getX().floatValue();
        }


        var xSpin = new Spinner<Double>(-10000000.000, 10000000.000, 0, 0.1);
        xSpin.setEditable(true);
        xSpin.setPromptText("Xvalue");

        var ySpin = new Spinner<Double>(-10000000.000, 10000000.000, 0, 0.1);
        ySpin.setEditable(true);
        ySpin.setPromptText("Yvalue");

        var addButton = new Button("Add");
        addButton.setOnAction(ae -> series.add(xSpin.getValue(), ySpin.getValue()));

        var rValButton = new Button("r Value");
        rValButton.setOnAction(ae -> rValuebox.setText(String.valueOf(arr1[1]) ) );

        var rButton = new Button("");
        rButton.setOnAction(ae -> {
                    Number[] array = (Number[]) arr.get(0);
                    Number[] array2 = (Number[]) arr.get(1);
                    float seriesList = (float) Statistics.getCorrelation(array,array2);
                    rBox.setText(String.valueOf(seriesList));
                });

        var testButton = new Button("test");
        testButton.setOnAction(ae -> {
            if (series.getItemCount() > 1) {
                double[] coefficients = Regression.getOLSRegression(dataset, 0);
                double b = coefficients[0]; // intercept
                double m = coefficients[1]; // slope
                double x = series.getDataItem(0).getXValue();
                trend.clear();
                trend.add(x, m * x + b);
                x = series.getDataItem(series.getItemCount() - 1).getXValue();
                trend.add(x, m * x + b);
                dataset.addSeries(trend);
            }
        });


        HBox xBox = new HBox();
        xBox.getChildren().addAll(domainLabels);

        HBox yBox = new HBox();
        yBox.getChildren().addAll(rangeLabels);

        var enter = new ToolBar(xBox, xSpin, yBox, ySpin, addButton, equation, rValButton, rValuebox, testButton,rButton, rBox);
        BorderPane.setAlignment(enter, Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setCenter(new ChartViewer(chart));
        root.setBottom(enter);

        stage.setTitle("ScatterAdd");
        stage.setScene(new Scene(root, 720, 480));
        stage.show();

    }
}
