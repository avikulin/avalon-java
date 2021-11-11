package lib;

import interfaces.Func;

public class Integral {
    public static double integral(Func function, double left, double right, int density){
        if (function == null){
            throw new IllegalArgumentException("Function reference must be set");
        }
        if (right <= left){
            throw new IllegalArgumentException("Illegal bounds defined");
        }

        if (density <= 0){
            throw new IllegalArgumentException("Density must be positive value over 0");
        }

        double res = 0;
        double deltaX = (right - left)/density;

        for (int i=0; i < density; i++){
            double x = left + deltaX/2;
            double y = function.exec(x);
            res += deltaX*y;
            left += deltaX;
        }
        return res;
    }
}
