import interfaces.Func;

import static lib.Integral.integral;

public class Main {
    private static final int DENSITY = 100;

    public static void main(String[] args) {
        System.out.println("\n\nIntegral functions evaluation:");
        System.out.println(String.format("\nAll integrations performed with " +
                "Δx = (right bound-left bound)/%d", DENSITY));

        System.out.print("\nExample 1. Invoking integration of the static " +
                "function \"Math.log(x)\" on interval [2, 5]: ");
        System.out.println(integral(Math::log, 2, 5, DENSITY));

        System.out.print("\nExample 2. Invoking integration of the internal functor-class " +
                "with \"Math.pow(x, y) + x\" " +
                "function on interval [3, 4]: ");
        System.out.println(integral(new FunctorA(), 3, 4, DENSITY));

        System.out.print("\nExample 3. Invoking integration of the anonymous functor-class " +
                "with \"Math.exp(-1 * x)\" " +
                "function on interval [0.01, 2]: ");
        System.out.println(integral(new Func() {
                                        @Override
                                        public double exec(double value) {
                                            return Math.exp(-1 * value);
                                        }
                                    },
                0.01, 2, DENSITY));

        System.out.print("\nExample 4. Invoking instance method with \"Math.pow(x, 2)\" " +
                "function from the another internal functor-class on interval [1, 3]:");
        System.out.println(integral(new FunctorB()::exec, 1, 3, DENSITY));

        System.out.print("\nExample 5. Invoking λ-method with \"x*Math.sin(x)\" function on interval [0, 1]:");
        System.out.println(integral(x -> x * Math.sin(x), 0, 1, DENSITY));

        System.out.println();
        System.out.println("---done---");
    }

    private static class FunctorA implements Func {
        @Override
        public double exec(double value) {
            return Math.pow(value, 2) + value;
        }
    }

    private static class FunctorB implements Func {
        @Override
        public double exec(double value) {
            return Math.pow(value, 2);
        }
    }
}
