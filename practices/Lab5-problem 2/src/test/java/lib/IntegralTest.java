package lib;

import org.junit.jupiter.api.Test;

import static lib.Integral.integral;
import static org.junit.jupiter.api.Assertions.*;

class IntegralTest {

    @Test
    void ParametersCheckingTest() {
        assertThrows(IllegalArgumentException.class, () -> integral(null, 0, 1, 100),
                "Null-pointer function");
        assertThrows(IllegalArgumentException.class, () -> integral(f -> f, 10, 10, 100),
                "Empty interval of integration");
        assertThrows(IllegalArgumentException.class, () -> integral(f -> f, 10, 9.5, 100),
                "Inverted interval of integration");
        assertThrows(IllegalArgumentException.class, () -> integral(f -> f, 10, 10, 0),
                "Incorrect density param");
        assertThrows(IllegalArgumentException.class, () -> integral(f -> f, 10, 10, -1),
                "Incorrect density param");
    }

    @Test
    void CalculationTest() {
        final int DENSITY = 100;
        final double EPS = 1e-6;
        assertTrue(integral(x -> Math.log(x), 2, 5, DENSITY) - 3.660906451 < EPS);
        assertTrue(integral(x -> Math.pow(x, 2) + x, 3, 4, DENSITY) - 15.833325 < EPS);
        assertTrue(integral(x -> Math.exp(-1 * x), 0.01, 2, DENSITY) - 0.854700 < EPS);
        assertTrue(integral(x -> Math.pow(x, 2), 1, 3, DENSITY) - 8.66660 < EPS);
        assertTrue(integral(x -> x * Math.sin(x), 0, 1, DENSITY) - 0.301163 < EPS);
    }
}