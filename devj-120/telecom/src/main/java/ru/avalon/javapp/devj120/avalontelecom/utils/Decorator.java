package ru.avalon.javapp.devj120.avalontelecom.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

public class Decorator {
    public static Object getValue(Supplier<Object> func) {
        Object value = func.get();
        return (value == null) ? "n\\a" : value;
    }

    public static String getValue(Supplier<LocalDate> func, String format) {
        LocalDate value = func.get();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(format);
        return (value == null) ? "n\\a" : value.format(dateFormat);
    }
}
