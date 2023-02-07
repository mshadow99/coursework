package Grava;

public class Calc extends Grava{
    public static double correlation(){
        //System.out.println(xList.size());
        //System.out.print(xList.get(2));

        double sx = 0.0;
        double sy = 0.0;
        double sxx = 0.0;
        double syy = 0.0;
        double sxy = 0.0;
        double initial = 0.0;

        double sigmaX = 0.0;
        double sigmaY = 0.0;
        double sigmaXX = 0.0;
        double sigmaYY = 0.0;
        double sigmaXY = 0.0;

        int n = xList.size();

        for(int i = 0; i < n; ++i) {
            double x = (double) xList.get(i);
            double y = (double) yList.get(i);

            sigmaX = sigmaX + x;
            sigmaY = sigmaY + y;
            sigmaXX = sigmaXX + (x*x);
            sigmaYY = sigmaYY + (y*y);
            sigmaXY = sigmaXY + (x*y);

            sx += x;
            sy += y;
            sxx += x * x;
            syy += y * y;
            sxy += x * y;
        }

        // covariation
        /*
        double cov = sxy / n - sx * sy / n / n;
        // standard error of x
        double sigmax = Math.sqrt(sxx / n -  sx * sx / n / n);
        // standard error of y
        double sigmay = Math.sqrt(syy / n -  sy * sy / n / n);
        */
        double sigmaX2 = sigmaX*sigmaX;
        double sigmaY2= sigmaY* sigmaY;
        double sigmaXY2= sigmaX* sigmaY;
        double numerator = (n*sigmaXY)-(sigmaXY2);
        double denominator = Math.sqrt( ( (n*(sigmaXX))- (sigmaX2)) * (n*(sigmaYY) - (sigmaY2)) );
        double rVal = (numerator/denominator);
        double cov = sigmaXY / n - sigmaX * sigmaY/ n / n ;
        // standard error of x
        double sigmax = Math.sqrt(sigmaXX / n -  sigmaX * sigmaX / n / n );
        // standard error of y
        double sigmay = Math.sqrt(sigmaYY / n -  sigmaY * sigmaY / n / n );

        //double rVal = (cov / sigmax / sigmay);
        return rVal;

    }
}
