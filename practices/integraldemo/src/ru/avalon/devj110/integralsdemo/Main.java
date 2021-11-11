package ru.avalon.devj110.integralsdemo;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Main {
    private static final int DENSITY = 100;
    public static void main(String[] args) {

        try {
            System.out.println("Performing some basic unit-tests:");
            try{
                System.out.println("\ntest #1. Null reference in function param");
                integral(null, 0, 1, 100);
                throw new AssertionError("NullPointerException must be rise");
            }catch (IllegalArgumentException iae){
                System.out.println("\tpassed");
            }
            try{
                System.out.println("\ntest #2. Null-length interval of integration");
                integral(f->f, 10, 10, 100);
                throw new AssertionError("IllegalArgumentException must be rise");
            }catch (IllegalArgumentException iae){
                System.out.println("\tpassed");
            }

            try{
                System.out.println("\ntest #3. Inverted interval of integration");
                integral(f->f, 10, 9.5, 100);
                throw new AssertionError("IllegalArgumentException must be rise");
            }catch (IllegalArgumentException iae){
                System.out.println("\tpassed");
            }

            try{
                System.out.println("\ntest #4. Zero density of integration");
                integral(f->f, 10, 10, 0);
                throw new AssertionError("IllegalArgumentException must be rise");
            }catch (IllegalArgumentException iae){
                System.out.println("\tpassed");
            }
        }catch (RuntimeException|AssertionError ex){
            String msg = ex.getMessage();
            msg = (msg==null)?"no details":msg;

            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            ex.printStackTrace(printWriter);
            String stackTrace = stringWriter.toString();

            System.out.println(String.format("\tfailed! Details: %s\n\tStack trace:\n\t%s", msg, stackTrace));
        }

        System.out.println("\n\nIntegral functions evaluation:");
        System.out.println(integral(x->Math.log(x), 2,5, DENSITY));
        System.out.println(integral(x->Math.pow(x,2)+x, 3,4, DENSITY));
        System.out.println(integral(x->Math.exp(-1*x), 0.01,2, DENSITY));
        System.out.println(integral(x->Math.pow(x,2), 1,3, DENSITY));
        System.out.println(integral(x->x*Math.sin(x), 0,1, DENSITY));
    }

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

        double eps = 1e-6;
        double res = 0;
        double deltaX = (right - left)/density;
        while (right - left > eps){
            double x = left + deltaX/2;
            double y = function.exec(x);
            /*System.out.println(String.format("\t left = %f x=%f y=%f s=%f", left, x, y, deltaX*y));*/
            res += deltaX*y;
            left += deltaX;
        }
        return res;
    }
}
