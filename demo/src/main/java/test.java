package Grava;

import javafx.beans.property.SimpleDoubleProperty;

public class tableClass {

    private SimpleDoubleProperty xCol;
    private SimpleDoubleProperty yCol;
    // private property objects yCol and xCol created
    public tableClass(double xCol, double yCol) {
        this.xCol = new SimpleDoubleProperty(xCol);
        this.yCol = new SimpleDoubleProperty(yCol);
        //returns value of xCol and yCol
    }

    public double getX(){
        return xCol.get();
}

public void setX(int x){
        this.xCol = new SimpleDoubleProperty (x);
}

public double getY(){
        return yCol.get();
}

public void setY(int y){
        this.yCol = new SimpleDoubleProperty(y);
}
//getters and setters used to obtain value in the Grava class
}
