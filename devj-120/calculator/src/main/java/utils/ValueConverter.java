package utils;

import enums.CalculationMode;

import java.math.BigInteger;

public class ValueConverter {
    public static BigInteger fromString(String s, CalculationMode mode) throws IllegalArgumentException {
        try{
            BigInteger value = new BigInteger(s, mode.getRadix());
            return value;
        } catch (NumberFormatException e) {
            String msg = String.format("\"%s\" is not a valid %s-value", s, mode);
            IllegalArgumentException exception = new IllegalArgumentException(msg);
            Tracer.get().logError(ValueConverter.class, e, "Error in string-to-value reflection");
            throw exception;
        }
    }

    public static String toString(BigInteger value, CalculationMode mode){
        try{
            String s = value.toString(mode.getRadix());
            return s;
        } catch (NumberFormatException e) {
            String msg = String.format("Can't convert value \"%d\" to %s-string", value, mode);
            IllegalArgumentException exception = new IllegalArgumentException(msg);
            Tracer.get().logError(ValueConverter.class, e, "Error in value-to-string reflection");
            throw exception;
        }
    }
}
