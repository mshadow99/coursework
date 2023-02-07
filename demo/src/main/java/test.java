package Grava;

import javafx.beans.property.SimpleDoubleProperty;

public class test {
    private SimpleDoubleProperty xCol;
    private SimpleDoubleProperty yCol;

    public test(double xCol, double yCol) {
        this.xCol = new SimpleDoubleProperty(xCol);
        this.yCol = new SimpleDoubleProperty(yCol);
    }

    public double getX() {
        return this.xCol.get();
    }

    public void setX(int x) {
        this.xCol = new SimpleDoubleProperty((double)x);
    }

    public double getY() {
        return this.yCol.get();
    }

    public void setY(int y) {
        this.yCol = new SimpleDoubleProperty((double)y);
    }
}