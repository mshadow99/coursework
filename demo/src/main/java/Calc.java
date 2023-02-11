package Grava;

import org.jfree.data.json.JSONUtils;

public class Calc extends Grava {
    public static double correlation() {

        double sigmaX = 0.0;
        double sigmaY = 0.0;
        double sigmaXX = 0.0;
        double sigmaYY = 0.0;
        double sigmaXY = 0.0;
        //sigma values instantiated
        int n = xList.size();
        //variable n created(length of list)
        for (int i = 0; i < n; ++i) {
            double x = (double) xList.get(i);
            double y = (double) yList.get(i);
            //both the value at x and y are created x,y
            sigmaX += x;
            sigmaY += y;
            //the x and y values are added through the length of the list to calculate the sum
            sigmaXX += (x * x);
            sigmaYY += + (y * y);
            //the x and y values are squared then added through the length of the list
            sigmaXY += + (x * y);
            //the x and y values are multiplied in order to find the sum of the x*y values.

        }

        double sigmaX2 = sigmaX * sigmaX;
        double sigmaY2 = sigmaY * sigmaY;
        double sigmaXY2 = sigmaX * sigmaY;

        double numerator = (n * sigmaXY) - (sigmaXY2);
        double denominator = Math.sqrt(((n * (sigmaXX)) - (sigmaX2)) * (n * (sigmaYY) - (sigmaY2)));
        double rVal = (numerator / denominator);
        //the correlation coefficient is calculated here with the pearson correlation coefficient formula
        //  covariance = (n* (sum of x*y values))-(sum of x values * sum of y values)
        // Standard deviation of x * standard deviation of y = ( n * (sum of x*x values) - (sum of x values)*(sum of x values) ) * ((n * (sum of y*y values)) - ((sum of y values)*(sum of y values)))
        // rVal(correlation coefficient) = covariance / (Standard deviation of x * standard deviation of y)
        return rVal;

    }
    public static double leastSquare() {

        double sigmaX = 0.00;
        double sigmaY = 0.00;
        double sigmaXX = 0.00;
        double sigmaXY = 0.00;
        //sigma values instantiated
        int n = xList.size();
        //variable n created(length of list)
        for (int i = 0; i < n; i++) {
            double x = (double) xList.get(i);
            double y = (double) yList.get(i);
            //both the value at x and y are created x,y
            sigmaX += x;
            sigmaY += y;
            //the x and y values are added through the length of the list to calculate the sum
            sigmaXX += (x * x);
            //the x values are squared then added through the length of the list
            sigmaXY += + (x * y);
            //the x and y values are multiplied in order to find the sum of the x*y values.

        }
        double sigmaX2 = sigmaX * sigmaX;
        double sigmaXY2 = sigmaX * sigmaY;


        double numerator = ((n * sigmaXY) - (sigmaXY2));
        double denominator = ((n * sigmaXX) - (sigmaX2));
        double b = (numerator / denominator);
        // numerator = (n * (sum of x*y values)) - (sum of x values * sum of y values)
        // denominator = (n * sum of x*x values) - (sum of x values * sum of x values)
        //least squares regression(b) = numerator/ denominator

        return b;

    }
    public static double intercept() {

        double sigmaX = 0.00;
        double sigmaY = 0.00;
        double sigmaXX = 0.00;
        double sigmaYY = 0.00;
        double sigmaXY = 0.00;
        //sigma values instantiated
        int n = xList.size();
        //variable n created(length of list)
        for (int i = 0; i < n; i++) {
            double x = (double) xList.get(i);
            double y = (double) yList.get(i);
            //both the value at x and y are created x,y
            sigmaX = sigmaX + x;
            sigmaY = sigmaY + y;
            //the x and y values are added through the length of the list to calculate the sum
            sigmaXX = sigmaXX + (x * x);
            sigmaYY = sigmaYY + (y * y);
            //the x and y values are squared then added through the length of the list
            sigmaXY = sigmaXY + (x * y);
            //the x and y values are multiplied in order to find the sum of the x*y values.
        }
        double sigmaX2 = sigmaX * sigmaX;
        double sigmaXY2 = sigmaX * sigmaY;
        double meanX = sigmaX/n;
        double meanY = sigmaY/n;
        // mean = sum of list/ total number of items in list
        double numerator = ((n * sigmaXY) - (sigmaXY2));
        double denominator = ((n * sigmaXX) - (sigmaX2));
        double b = (numerator / denominator);
        // numerator = (n * (sum of x*y values)) - (sum of x values * sum of y values)
        // denominator = (n * sum of x*x values) - (sum of x values * sum of x values)
        //least squares regression(b) = numerator/ denominator
        double a = meanY - (b*meanX);
        // c(intercept) = y - m*x
        return a;
    }
}
