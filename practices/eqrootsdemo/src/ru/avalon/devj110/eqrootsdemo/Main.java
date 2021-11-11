package ru.avalon.devj110.eqrootsdemo;

public class Main {
    public static void main(String[] args) {
        Func f2 = new Func() {
            public double exec(double value) {
                return Math.sin(value) - 0.75;
            }
        };


        System.out.println(findRoot(f2, 2, 3, 1e-5));
        Func f3 = x -> Math.pow(x, 3) - 8 * x + 2;
        System.out.println(findRoot(f3, 1, 5, 1e-5));
    }

    public static double findRoot(Func func, double left, double right, double accuracy) {
        while (right - left > accuracy) {
            double middle = (right + left) / 2;
            if (func.exec(middle) == 0) {
                return middle;
            }
            if (((func.exec(left) < 0) && (func.exec(middle) < 0)) ||
                    ((func.exec(left) > 0) && (func.exec(middle) > 0))) {
                left = middle;
            }
            if (((func.exec(middle) < 0) && (func.exec(right) < 0)) ||
                    ((func.exec(middle) > 0) && (func.exec(right) > 0))) {
                right = middle;
            }
        }
        return left;
    }
}
