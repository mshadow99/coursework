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
    private ObservableList<tableClass> testModel = FXCollections.observableArrayList(
    );
    //a list is created to hold the values inputted to be displayed to the table
    private JFreeChart createChart() {
        return ChartFactory.createScatterPlot("","Amps", "Volts", dataset);
    }
    //a scatterplot(createChart) is created and is a method object from the chartfactory class with name createChart
    @Override
    public void start(Stage stage) {
        //the start method is created from the application class with parameters stage
        Image image = new Image("Grava.logo.png");
        stage.getIcons().add(image);
        // the applications image icon is set
        HBox xBox = new HBox();
        HBox yBox = new HBox();
        //creates x and yBox application layouts

        var chart = createChart();
        //creates object of create chart method

        XYPlot plot = chart.getXYPlot();
        //creates object plot of getXYPlot() method
        dataset.addSeries(trend);
        //add the series trend to the dataset
        ValueMarker marker = new ValueMarker(0);
        // creates an object(marker of the ValueMarker() class with position 0,0
        marker.setPaint(Color.black);
        // sets color of the axis as black
        plot.addDomainMarker(marker);
        plot.addRangeMarker(marker);
        //adds the domain line and the range line to the plot

        XYLineAndShapeRenderer r = (XYLineAndShapeRenderer) plot.getRenderer();
        // creates an object of the .getRenderer method which is a straight line
        r.setSeriesLinesVisible(1, Boolean.TRUE);
        r.setSeriesShapesVisible(1, Boolean.FALSE);
        //sets the lines visibility as true and the lines plot shapes as false


        var equationField = new TextField();
        var gradientField = new TextField();
        var corField = new TextField();
        equationField.setText("Equation");
        gradientField.setText("Gradient");
        corField.setText("r-Value");
        equationField.setEditable(false);
        gradientField.setEditable(false);
        corField.setEditable(false);

        var deleteButton = new Button("clear last value");
        var addButton = new Button("Add");
        var clearButton = new Button("clear List");
        //button objects created

        var cb = new CheckBox("Trend");
        //creates a checkbox with label Trend
        //text-fields are created to view outputted data
        table = new TableView();
        // creates a table object of the TableView() class.
        var xSpin = new Spinner<Double>(-10000000.000, 10000000.000, 0, 0.1);
        xSpin.setEditable(true);
        xSpin.setPromptText("Xvalue");

        var ySpin = new Spinner<Double>(-10000000.000, 10000000.000, 0, 0.1);
        ySpin.setEditable(true);
        ySpin.setPromptText("Yvalue");
        // here both x and ySpin objects of spinner are created,where x and y values can be inputted
        // parameters of the spinners being (min value, max value, initial value, step count)

        TableColumn xCol = new TableColumn("X:");
        TableColumn yCol = new TableColumn("Y:");
        xCol.setMinWidth(150);
        yCol.setMinWidth(150);
        // here xCol and yCol objects with minimum width 150p  are created which hold the x and y values inputted
        xCol.setCellValueFactory(new PropertyValueFactory<>("X"));
        yCol.setCellValueFactory(new PropertyValueFactory<>("Y"));
        // here the xCol and yCol properties are set as X and Y
        DecimalFormat df = new DecimalFormat("0.000");
        // here a decimalFormat object is created to 3 decimal places

        series.addChangeListener((event) -> {
            double spinX = xSpin.getValue();
            double spinY = ySpin.getValue();
            // the double value of the spinner is set as spinX and spinY

            if (series.getItemCount() > 1) {
                // if statement to ensure there is a minimum of 2 values in the series
                double b = Calc.leastSquare();
                // gradient from the least square regression formula
                double a = Calc.intercept();
                // y intercept calculated with rearranging the y = mx + c formula
                float formatB = Float.parseFloat(df.format(b));
                float formatA = Float.parseFloat(df.format(a));
                // the values of a and b are formatted to 3 decimal places
                equationField.setText("y = " +  formatB + " + " + formatA);
                // the equation of the line is displayed to the text-field
                double x = series.getDataItem(0).getXValue();
                //the x value at index 0 is set to x
                trend.clear();
                // the trend series is cleared
                trend.add(x, b * x + a);
                //points x and y are added to the trend series
                x = series.getDataItem(series.getItemCount() - 1).getXValue();
                //x is set to the most recent item in the trend series
                trend.add(x, b * x + a);
                // the most recent value in the series is added to the trend
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
        // listeners created to change the charts axis labels and the table column titles in accordance with the choicebox labels

        domainLabels.getItems().addAll("Amps", "Seconds","Second","Metres","Extension","Strain","Kilograms","Metres³","Volts","Metres²");
        domainLabels.setValue("xLabel");
        //
        rangeLabels.getItems().addAll("Volts", "Metres","Metres/Second","Joules","Force","Stress","Momentum","Coulombs","Ohms","Pascals");
        rangeLabels.setValue("yLabel");
        //the choiceboxes are populated with items

        addButton.setOnAction(ae -> {
            double spinX = xSpin.getValue();
            double spinY = ySpin.getValue();
            xList.add(spinX);
            yList.add(spinY);
            testModel.add(new tableClass(spinX, spinY));
            series.add(spinX, spinY);
            // the double value of the spinner is added to the x and yLists, the testModel observableArrayList and the series


            if (series.getItemCount() > 1){
                double m = Calc.leastSquare();
                double cor = Calc.correlation();
                // objects created of methods of the calc class
                float roundedCor = Float.parseFloat(df.format(cor));
                float roundedM = Float.parseFloat(df.format(m));
                // float values to 3 decimal places created
                yLabel = rangeLabels.getValue();
                xLabel = domainLabels.getValue();
                // the value of the choiceboxes set as x and yLabel
                corField.setText(String.valueOf(roundedCor));
                // correlation coefficient outputted to textfield

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
                // multiple conditions created if that the x and y label match then the gradient gives a meaningful value(unit) if not then gradient displayed

            }
        });


        deleteButton.setOnAction(ae -> {
            ObservableList<tableClass> allSelected, itemSelected;
            allSelected = table.getItems();
            itemSelected = table.getSelectionModel().getSelectedItems();
            Object itemSelected1 = table.getSelectionModel().getSelectedItem();
            //2 list objects are created of the table
            if (itemSelected1 != null){// condition to check if an item is selected

                int n = (table.getSelectionModel().getSelectedIndex());
                //returns the index of the currently selected item in the table

                if (series.getItemCount() == 1) {// boolean expression to check if length of series is = to 1
                    series.clear();
                    trend.clear();
                    testModel.clear();
                    // clears series,trend and testModel list
                }else {
                    series.delete(n, n);
                    itemSelected.forEach(allSelected::remove);
                    xList.remove(n);
                    yList.remove(n);
                    //removes data item from the series with index n, removes item selected from the table and removes data items with index n from x and yList

                }

            }

        } );

        clearButton.setOnAction(e -> {
            e.consume();
            clearList();
            //runs the clearList() method if the clearButton is selected
        });

        var g = chart.getXYPlot().getRenderer();
        // creates g object pf XYItemRenderer

        //creates
        cb.setSelected(true);//checkbox ticked
        cb.selectedProperty().addListener((o) -> {
            // listener lambda expression used to check if the checkbox is ticked
            g.setSeriesVisible(1, cb.isSelected());
            // changes the visibility of the trend series when the checkbox is selected
        });

        table.setItems(testModel);
        table.getColumns().addAll(yCol,xCol);
        //the list tesModel is added to the table and the columns are added to the table

        xBox.getChildren().addAll(domainLabels);
        yBox.getChildren().addAll(rangeLabels);
        //adds the choiceboxes to the application
        var enter = new ToolBar(cb,xBox, xSpin, yBox, ySpin, addButton, equationField,gradientField ,corField,deleteButton,clearButton);
        //toolbar layout created and populated with items
        BorderPane.setAlignment(enter, Pos.CENTER);
        BorderPane root = new BorderPane();
        root.setCenter(new ChartViewer(chart));
        root.setRight(table);
        root.setBottom(enter);
        //root object created of BorderPane() class and the 3 items table,chart and enter are added
        stage.setTitle("Grava");
        stage.setScene(new Scene(root, 1280, 700));
        stage.setMinHeight(500);
        stage.setMinWidth(900);
        stage.show();
        //the applications title and size is set
    }

    private void clearList(){
        Boolean answer = ConfirmBox.display("Clear","Are you sure you want to clear all values?");
        // answer created to confirm button press
        if(answer) {
            series.clear();
            trend.clear();
            testModel.clear();
            // if the answer = true then the series,trend and testmodel list is cleared
        }
    }

}
