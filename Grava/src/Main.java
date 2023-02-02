package Grava;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class Grava extends Application {


    private static final XYSeries series = new XYSeries("Voltage");
    private final XYSeries trend = new XYSeries("Trend");
    private final XYSeriesCollection dataset = new XYSeriesCollection(series);

    TableView table;

    ChoiceBox<String> domainLabels = new ChoiceBox<>();
    ChoiceBox<String> rangeLabels = new ChoiceBox<>();
    List seriesList = series.getItems();
    List xList = new ArrayList();

    double xArr[];
    double yArr[];
    private ObservableList<test> testModel = FXCollections.observableArrayList(
    );
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
        //XYLineAndShapeRenderer r = new XYLineAndShapeRenderer(false,true);
        r.setSeriesLinesVisible(1, Boolean.TRUE);
        r.setSeriesShapesVisible(1, Boolean.FALSE);

        var equationField = new TextField();
        var gradientField = new TextField();
        table = new TableView();

        var xSpin = new Spinner<Double>(-10000000.000, 10000000.000, 0, 0.1);
        xSpin.setEditable(true);
        xSpin.setPromptText("Xvalue");

        var ySpin = new Spinner<Double>(-10000000.000, 10000000.000, 0, 0.1);
        ySpin.setEditable(true);
        ySpin.setPromptText("Yvalue");



        //System.out.println(xList.size());

        TableColumn xCol = new TableColumn("X:");
        TableColumn yCol = new TableColumn("Y:");
        xCol.setMinWidth(150);
        yCol.setMinWidth(150);
        xCol.setCellValueFactory(new PropertyValueFactory<>("X"));
        yCol.setCellValueFactory(new PropertyValueFactory<>("Y"));

        DecimalFormat df = new DecimalFormat("0.000");


        series.addChangeListener((event) -> {
            double spinX = xSpin.getValue();
            double spinY = ySpin.getValue();
            testModel.add(new test(spinX, spinY));

            if (series.getItemCount() > 1) {
                double[] coefficients = Regression.getOLSRegression(dataset, 0);
                //double[] coefficients = Regression.getPowerRegression(dataset, 0);
                double c = coefficients[0]; // intercept
                double m = coefficients[1]; // slope
                float formatM = Float.parseFloat(df.format(m));
                float formatC = Float.parseFloat(df.format(c));

                equationField.setText("y = " + formatM + " x + " + formatC);
                double x = series.getDataItem(0).getXValue();
                trend.clear();
                trend.add(x, m * x + c);
                x = series.getDataItem(series.getItemCount() - 1).getXValue();
                trend.add(x, m * x + c);
                //equation.setText(String.valueOf(series.getDataItem(0).getXValue()));

            }
            //double[] coefficients = Regression.getOLSRegression(dataset, 0);
            //double m = coefficients[1]; // slope


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



        var addButton = new Button("Add");
        addButton.setOnAction(ae -> {

                series.add(xSpin.getValue(), ySpin.getValue());
                xList.add(xSpin.getValue());
                String yLabel = rangeLabels.getValue();
                String xLabel = domainLabels.getValue();

                    if (series.getItemCount() > 1) {
                        //xArr[1] = ;
                    }

            if (series.getItemCount() > 1){
                double[] coefficients = Regression.getOLSRegression(dataset, 0);
                double m = coefficients[1]; // slope
                float formatM = Float.parseFloat(df.format(m));
                if ((xLabel == ("Current")) && (yLabel == ("Voltage"))) {
                    gradientField.setText(formatM + "Ω");
                } else if ((xLabel == ("Metres")) && (yLabel == ("Seconds"))) {
                    gradientField.setText(formatM + "M/S");
                } else {
                    gradientField.setText(String.valueOf(formatM));
                }
            }
        });

        //List seriesList = series.getItems();
        /*for (var Single_Value: seriesList)
        {
            var temp = Single_Value;
        }*/
/*
        var gradButton = new Button("grad");
        gradButton.setOnAction(ae -> {
        });*/


        var testButton = new Button("testBut");
        testButton.setOnAction(ae -> {
            System.out.println(xList.size());
            System.out.print(xList.get(1));
        } );

        var deleteButton = new Button("clear last value");
        deleteButton.setOnAction(ae -> {
            double i = testModel.size()-1;
            double z = series.getItemCount();

            //System.out.println(series.getItemCount());
            testModel.remove((int) i,(int) i);
            //testModel.add((int) i,null);

            table.refresh();

            series.delete((int) i,(int) i);

        } );

        var g = chart.getXYPlot().getRenderer();
        var cb = new CheckBox("Trend");
        cb.setSelected(true);
        cb.selectedProperty().addListener((o) -> {
            //if (series.getItemCount() > 0) {
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
            //}
        });

        table.setItems(testModel);
        table.getColumns().addAll(xCol,yCol);

        HBox xBox = new HBox();
        xBox.getChildren().addAll(domainLabels);

        HBox yBox = new HBox();
        yBox.getChildren().addAll(rangeLabels);

        var enter = new ToolBar(cb,xBox, xSpin, yBox, ySpin, addButton, equationField,gradientField, deleteButton, testButton);
        BorderPane.setAlignment(enter, Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setCenter(new ChartViewer(chart));
        root.setRight(table);
        root.setBottom(enter);

        stage.setTitle("ScatterAdd");
        stage.setScene(new Scene(root, 960, 500));
        stage.show();

    }
    /*public ObservableList getXY(){
        ObservableList<test> testModel = FXCollections.observableArrayList();
        testModel.add(new test(1,2));
        testModel.add(new test(3,4));
        testModel.add()
    /*new test(1,2),
                new test(2,3),
        return testModel;
    }*/
}
