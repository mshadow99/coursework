package Grava;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.fx.ChartViewer;
import java.awt.*;

public class  graph extends Application{

        public static void main(String[] args) {
                launch(args);
        }

        public static JFreeChart createChart() {

                XYSeriesCollection dataset = new XYSeriesCollection();

                XYSeries series1 = new XYSeries("Voltage");
                series1.add(10, 35);
                series1.add(11, 37);
                series1.add(12, 41);
                series1.add(12, 39);
                series1.add(13, 44);
                series1.add(14, 51);
                series1.add(15, 52);
                series1.add(15, 54);
                series1.add(16, 57);
                series1.add(17, 62);
                series1.add(17, 66);
                series1.add(18, 70);

                dataset.addSeries(series1);

                JFreeChart scatterPlot = ChartFactory.createScatterPlot(
                        "VI Characteristics graph", // Chart title
                        "Voltage", // X-Axis Label
                        "Current", // Y-Axis Label
                        dataset // Dataset for the Chart
                );
                
                
                return scatterPlot;
        }

        @Override
        public void start(Stage stage) throws Exception {
                GridPane grid = new GridPane();
                grid.setPadding(new Insets(10, 10, 10, 100));
                grid.setVgap(8);
                Button button = new Button("hey click me");
                ChartViewer viewer = new ChartViewer(createChart());
                stage.setScene(new Scene(viewer));
                Scene scene = new Scene(grid, 300, 200);
                stage.setTitle("VI Characteristics graph");
                stage.setWidth(600);
                stage.setHeight(400);
                stage.show();
        }
}
