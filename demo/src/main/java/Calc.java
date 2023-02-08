package Grava;

import org.jfree.data.json.JSONUtils;

public class Calc extends Grava {
    public static double correlation() {

        double sigmaX = 0.0;
        double sigmaY = 0.0;
        double sigmaXX = 0.0;
        double sigmaYY = 0.0;
        double sigmaXY = 0.0;

        int n = xList.size();

        for (int i = 0; i < n; ++i) {
            double x = (double) xList.get(i);
            double y = (double) yList.get(i);

            sigmaX = sigmaX + x;
            sigmaY = sigmaY + y;
            sigmaXX = sigmaXX + (x * x);
            sigmaYY = sigmaYY + (y * y);
            sigmaXY = sigmaXY + (x * y);


        }

        double sigmaX2 = sigmaX * sigmaX;
        double sigmaY2 = sigmaY * sigmaY;
        //double sigmaX2 = sigmaX * sigmaX;
        //double sigmaY2 = sigmaY * sigmaY;
        double sigmaXY2 = sigmaX * sigmaY;
        double numerator = (n * sigmaXY) - (sigmaXY2);
        double denominator = Math.sqrt(((n * (sigmaXX)) - (sigmaX2)) * (n * (sigmaYY) - (sigmaY2)));
        double rVal = (numerator / denominator);

        return rVal;

    }
    public static double vegan() {

        double sigmaX = 0.00;
        double sigmaY = 0.00;
        double sigmaXX = 0.00;
        double sigmaYY = 0.00;
        double sigmaXY = 0.00;

        int n = xList.size();

        for (int i = 0; i < n; i++) {
            double x = (double) xList.get(i);
            double y = (double) yList.get(i);

            sigmaX = sigmaX + x;
            sigmaY = sigmaY + y;
            sigmaXX = sigmaXX + (x * x);
            sigmaYY = sigmaYY + (y * y);
            sigmaXY = sigmaXY + (x * y);

        }
        double sigmaX2 = sigmaX * sigmaX;
        double sigmaXY2 = sigmaX * sigmaY;




        double numerator = ((n * sigmaXY) - (sigmaXY2));
        double denominator = ((n * sigmaXX) - (sigmaX2));
        double b = (numerator / denominator);
        //double rVal = (numerator / denominator);

        return b;

    }

}
