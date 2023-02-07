package Grava;

import java.awt.*;
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
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.statistics.Regression;;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;


public class Grava extends Application {


    private static final XYSeries series = new XYSeries("Voltage");
    private final XYSeries trend = new XYSeries("Trend");
    private final XYSeriesCollection dataset = new XYSeriesCollection(series);

    TableView table;

    ChoiceBox<String> domainLabels = new ChoiceBox<>();
    ChoiceBox<String> rangeLabels = new ChoiceBox<>();
    List seriesList = series.getItems();
    static List xList = new ArrayList();
    static List yList = new ArrayList();

    double xArr[];
    double yArr[];
    private ObservableList<test> testModel = FXCollections.observableArrayList(
    );
    private JFreeChart createChart() {
        return ChartFactory.createScatterPlot("Grava", "Amps", "Volts", dataset);
    }

    @Override
    public void start(Stage stage) {

        Image image = new Image("Grava.logo.png");
        stage.getIcons().add(image);
        var chart = createChart();


        XYPlot plot = chart.getXYPlot();
        dataset.addSeries(trend);
        ValueMarker marker = new ValueMarker(0);  // position is the value on the axis
        marker.setPaint(Color.black);
        /*
        float dash1[] = {10.0f};

        BasicStroke dashed = new BasicStroke(1,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                1, dash1, 2);
        marker.setStroke(dashed);

         */
        plot.addDomainMarker(marker);
        plot.addRangeMarker(marker);

        //plot.setDomainCrosshairVisible(true);
        //plot.setRangeCrosshairVisible(true);


        plot.setRangeCrosshairValue(0.0);
        plot.setDomainCrosshairValue(0.0);
        plot.setDomainCrosshairLockedOnData(true);
        XYLineAndShapeRenderer r = (XYLineAndShapeRenderer) plot.getRenderer();
        //XYLineAndShapeRenderer r = new XYLineAndShapeRenderer(false,true);
        r.setSeriesLinesVisible(1, Boolean.TRUE);
        r.setSeriesShapesVisible(1, Boolean.FALSE);

        String yLabel = rangeLabels.getValue();
        String xLabel = domainLabels.getValue();

        var equationField = new TextField();
        var gradientField = new TextField();
        var corField = new TextField();
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
            xCol.setText(s1);
        });
        rangeLabels.getSelectionModel().selectedItemProperty().addListener((ov, s0, s1) -> {
            chart.getXYPlot().getRangeAxis().setLabel(s1);
            yCol.setText(s1);
        });
        //if chart.getXYPlot().getRangeAxis(). == ("");

        if((xLabel == ("Amps")) && (yLabel == ("Volts"))) {
        chart.getTitle().setText("ohms");

    }
        domainLabels.getItems().addAll("Amps", "Seconds","Coulombs");
        domainLabels.setValue("Amps");

        rangeLabels.getItems().addAll("Volts", "Metres");
        rangeLabels.setValue("Volts");



        var addButton = new Button("Add");
        addButton.setOnAction(ae -> {
            double spinX = xSpin.getValue();
            double spinY = ySpin.getValue();
            testModel.add(new test(spinX, spinY));
                series.add(spinX, spinY);
                xList.add(spinX);
                yList.add(spinY);


                    if (series.getItemCount() > 1) {
                        //xArr[1] = ;
                    }

            if (series.getItemCount() > 1){
                double[] coefficients = Regression.getOLSRegression(dataset, 0);
                double m = coefficients[1]; // slope
                float formatM = Float.parseFloat(df.format(m));
                if ((xLabel == ("Amps")) && (yLabel == ("Volts"))) {
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
             corField.setText(String.valueOf(Calc.correlation()));
            //System.out.println(xList.size());
        } );

        var deleteButton = new Button("clear last value");
        deleteButton.setOnAction(ae -> {
            double i = testModel.size() - 1;
            double z = series.getItemCount();
            ObservableList<test> allSelected, itemSelected;
            allSelected = table.getItems();
            itemSelected = table.getSelectionModel().getSelectedItems();
            Object itemSelected1 = table.getSelectionModel().getSelectedItem();
            if (itemSelected1 != null){
                // ObservableList o = (table.getSelectionModel().getSelectedItems());
                //System.out.println(table.getSelectionModel().getSelectedIndex() + 1);
                int n = (table.getSelectionModel().getSelectedIndex());
                //series.delete(n, n);
            //itemSelected.forEach(allSelected::remove);
            //table.refresh();

            //xList.remove(n);
            //yList.remove(n);

            if (series.getItemCount() == 1) {
                series.clear();
                trend.clear();
                testModel.clear();
            }else {
                series.delete(n, n);
                itemSelected.forEach(allSelected::remove);
                //table.refresh();

                xList.remove(n);
                yList.remove(n);

                }

            //System.out.println(series.getItemCount());
            //testModel.getItem().remove((int) i,(int) i);
            //testModel.remove(i);
            //testModel.add((int) i,null);

            //table.refresh();

            //series.delete((int) i,(int) i);
        }

        } );

        var clearButton = new Button("clearList");
        clearButton.setOnAction(e -> {
            e.consume();
            clearList();
        });

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

        var enter = new ToolBar(cb,xBox, xSpin, yBox, ySpin, addButton, equationField,gradientField, deleteButton, testButton,corField,clearButton);
        BorderPane.setAlignment(enter, Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setCenter(new ChartViewer(chart));
        root.setRight(table);
        root.setBottom(enter);

        stage.setTitle("ScatterAdd");
        stage.setScene(new Scene(root, 1280, 700));
        stage.setMinHeight(500);
        stage.setMinWidth(900);
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
    private void clearList(){
        Boolean answer = ConfirmBox.display("Clear","Are you sure you want to clear all values?");
        if(answer) {
            series.clear();
            trend.clear();
            testModel.clear();
        }
    }

}
