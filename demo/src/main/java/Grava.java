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
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.statistics.Regression;;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class Grava extends Application {
//Grava inherits properties from JavaFx Application Class

    private static final XYSeries series = new XYSeries("");
    //a series(series) of x and y values are created in form of a 2d data structure ((x,y))
    //XYSeries from jfreechart library
    private final XYSeries trend = new XYSeries("Trend");
    //a second series(trend) of x and y values are created in form of a 2d data structure ((x,y))
    private final XYSeriesCollection dataset = new XYSeriesCollection(series);
    //a collection of series are created called dataset and (series) is added to the dataset
    TableView table;
    //a tableView table from the javaFx library is created
    ChoiceBox<String> domainLabels = new ChoiceBox<>();
    ChoiceBox<String> rangeLabels = new ChoiceBox<>();
    //a choicebox dropdown menu of the javafx library is created

    static List xList = new ArrayList();
    //a list to old x values
    static List yList = new ArrayList();
    //a list to hold y values
    String yLabel = rangeLabels.getValue();
    //obtains the value of the rangeLabels
    String xLabel = domainLabels.getValue();
    //obtains the value of the domainLabels
    private ObservableList<test> testModel = FXCollections.observableArrayList(
    );
    //a list is created to hold the values inputted to be displayed to the table
    private JFreeChart createChart() {
        return ChartFactory.createScatterPlot("","Amps", "Volts", dataset);
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

        plot.addDomainMarker(marker);
        plot.addRangeMarker(marker);



        plot.setRangeCrosshairValue(0.0);
        plot.setDomainCrosshairValue(0.0);
        plot.setDomainCrosshairLockedOnData(true);
        XYLineAndShapeRenderer r = (XYLineAndShapeRenderer) plot.getRenderer();

        r.setSeriesLinesVisible(1, Boolean.TRUE);
        r.setSeriesShapesVisible(1, Boolean.FALSE);



        var equationField = new TextField();
        var gradientField = new TextField();
        var corField = new TextField();
        equationField.setText("Equation");
        gradientField.setText("Gradient");
        corField.setText("r-Value");
        equationField.setEditable(false);
        gradientField.setEditable(false);
        corField.setEditable(false);
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
                double c = coefficients[0]; // intercept
                double m = coefficients[1]; // slope
                double b = Calc.leastSquare();
                double a = Calc.intercept();
                float formatM = Float.parseFloat(df.format(m));
                float formatC = Float.parseFloat(df.format(c));
                float formatB = Float.parseFloat(df.format(b));
                float formatA = Float.parseFloat(df.format(a));

                equationField.setText("y = " +  formatB + " + " + formatA);

                double x = series.getDataItem(0).getXValue();
                trend.clear();
                trend.add(x, m * x + c);
                x = series.getDataItem(series.getItemCount() - 1).getXValue();
                trend.add(x, m * x + c);
            }
        });


        domainLabels.getSelectionModel().selectedItemProperty().addListener((ov, s0, s1) -> {
            chart.getXYPlot().getDomainAxis().setLabel(s1);
            xCol.setText(s1);
        });

        rangeLabels.getSelectionModel().selectedItemProperty().addListener((ov, s0, s1) -> {
            chart.getXYPlot().getRangeAxis().setLabel(s1);
            yCol.setText(s1);
        });


        domainLabels.getItems().addAll("Amps", "Seconds","Second","Metres","Extension","Strain","Kilograms","Metres³","Volts","Metres²");
        domainLabels.setValue("xLabel");

        rangeLabels.getItems().addAll("Volts", "Metres","Metres/Second","Joules","Force","Stress","Momentum","Coulombs","Ohms","Pascals");
        rangeLabels.setValue("yLabel");



        var addButton = new Button("Add");
        addButton.setOnAction(ae -> {
            double spinX = xSpin.getValue();
            double spinY = ySpin.getValue();
            xList.add(spinX);
            yList.add(spinY);
            testModel.add(new test(spinX, spinY));
            series.add(spinX, spinY);


            if (series.getItemCount() > 1){
                double m = Calc.leastSquare(); // slope
                float roundedCore = Float.parseFloat(df.format(Calc.correlation()));
                float roundedM = Float.parseFloat(df.format(m));
                yLabel = rangeLabels.getValue();
                xLabel = domainLabels.getValue();
                corField.setText(String.valueOf(roundedCore));
                if ((xLabel == ("Amps")) && (yLabel == ("Volts"))) {

                    gradientField.setText(roundedM +"Ω");
                } else if ((xLabel == ("Seconds")) && (yLabel == ("Metres"))) {
                    gradientField.setText(roundedM + "M/s");

                } else if ((xLabel == ("Seconds")) && (yLabel == ("Metres/Seconds"))) {
                    gradientField.setText(roundedM + "M/s²");

                } else if ((xLabel == ("Seconds")) && (yLabel == ("Joules"))) {
                    gradientField.setText(roundedM + "W");

                } else if ((xLabel == ("Metres")) && (yLabel == ("Force"))) {
                    gradientField.setText(roundedM + "N/m");

                } else if ((xLabel == ("Strain")) && (yLabel == ("Stress"))) {
                    gradientField.setText(roundedM + "Pa");

                } else if ((xLabel == ("Seconds")) && (yLabel == ("Momentum"))) {
                    gradientField.setText(roundedM + "N");

                } else if ((xLabel == ("Seconds")) && (yLabel == ("Coulombs"))) {
                    gradientField.setText(roundedM + "A");

                } else if ((xLabel == ("Coulombs")) && (yLabel == ("Joules"))) {
                    gradientField.setText(roundedM + "V");

                } else if ((xLabel == ("Seconds")) && (yLabel == ("Ohms"))) {
                    gradientField.setText(roundedM + "ρ/m²");

                } else if ((xLabel == ("Kilograms")) && (yLabel == ("Joules"))) {
                    gradientField.setText(roundedM + "J/Kg");

                } else if ((xLabel == ("Volts")) && (yLabel == ("Coulombs"))) {
                    gradientField.setText(roundedM + "F");

                } else if ((xLabel == ("Metres²")) && (yLabel == ("Force"))) {
                    gradientField.setText(roundedM + "F");

                } else {
                    gradientField.setText(String.valueOf(roundedM));
                }


            }
        });

        var deleteButton = new Button("clear last value");
        deleteButton.setOnAction(ae -> {
            double i = testModel.size() - 1;
            double z = series.getItemCount();
            ObservableList<test> allSelected, itemSelected;
            allSelected = table.getItems();
            itemSelected = table.getSelectionModel().getSelectedItems();
            Object itemSelected1 = table.getSelectionModel().getSelectedItem();
            if (itemSelected1 != null){

                int n = (table.getSelectionModel().getSelectedIndex());


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

            }

        } );

        var clearButton = new Button("clear List");
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

            //}
        });

        table.setItems(testModel);
        table.getColumns().addAll(yCol,xCol);

        HBox xBox = new HBox();
        xBox.getChildren().addAll(domainLabels);

        HBox yBox = new HBox();
        yBox.getChildren().addAll(rangeLabels);

        var enter = new ToolBar(cb,xBox, xSpin, yBox, ySpin, addButton, equationField,gradientField ,corField,deleteButton,clearButton);
        BorderPane.setAlignment(enter, Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setCenter(new ChartViewer(chart));
        root.setRight(table);
        root.setBottom(enter);

        stage.setTitle("Grava");
        stage.setScene(new Scene(root, 1280, 700));
        stage.setMinHeight(500);
        stage.setMinWidth(900);
        stage.show();

    }

    private void clearList(){
        Boolean answer = ConfirmBox.display("Clear","Are you sure you want to clear all values?");
        if(answer) {
            series.clear();
            trend.clear();
            testModel.clear();
        }
    }

}
