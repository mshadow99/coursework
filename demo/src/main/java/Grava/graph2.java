package Grava;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.chart.util.ShapeUtils;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.plot.PlotOrientation;
import java.awt.Color;
import java.awt.Dimension;
import java.util.*;
import java.awt.Shape;


import java.awt.*;
import java.io.File;
import java.io.IOException;


public abstract class graph2 extends Application{
    Stage window;
    public static void main(String[] args) {
        launch(args);
    }

    public XYDataset createDataset() {

        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series1 = new XYSeries("Object 1");
        XYSeries series2 = new XYSeries("Object 2");
        XYSeries series3 = new XYSeries("Object 3");

        series1.add(1.0, 2.0);
        series1.add(2.0, 3.0);
        series1.add(3.0, 2.5);
        series1.add(3.5, 2.8);
        series1.add(4.2, 6.0);

        series2.add(2.0, 1.0);
        series2.add(2.5, 2.4);
        series2.add(3.2, 1.2);
        series2.add(3.9, 2.8);
        series2.add(4.6, 3.0);

        series3.add(1.2, 4.0);
        series3.add(2.5, 4.4);
        series3.add(3.8, 4.2);
        series3.add(4.3, 3.8);
        series3.add(4.5, 4.0);

        dataset.addSeries(series1);
        dataset.addSeries(series2);
        dataset.addSeries(series3);

        XYDataset scatterPlot = (XYDataset) ChartFactory.createScatterPlot("Grava",
                "Voltage", "Current", dataset);
        Shape cross = ShapeUtils.createDiagonalCross(3, 1);
        return scatterPlot;
    }

    @Override
    public void start(Stage stage) throws Exception {
        ChartViewer viewer = new ChartViewer((JFreeChart) createDataset());
        stage.setScene(new Scene(viewer));
        stage.setTitle("Grava");
        stage.setWidth(600);
        stage.setHeight(400);
        stage.show();
    }
}


