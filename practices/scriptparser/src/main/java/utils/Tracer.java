package utils;

import contracts.businesslogic.utils.DiagnosticLogger;
import enums.EventType;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Tracer implements DiagnosticLogger {
    private static Tracer tracerInstance;

    private StringBuilder logStorage;
    private DateTimeFormatter dtf;
    private String template = "%4d) %s %s [%s]: %s";
    private int lineNumber;
    private Tracer() {
        logStorage = new StringBuilder();
        lineNumber = 0;
        dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.ms");
    }

    public static DiagnosticLogger get(){
        if (tracerInstance == null){
            tracerInstance = new Tracer();
        }
        return tracerInstance;
    }

    @Override
    public void logError(Class<?> source, String... event) {
        String s = String.join(" ",event);
        trace(source, EventType.ERROR,s);
    }

    @Override
    public void logError(Class<?> source, Exception e, String... event) {
        logError(source, event);
        StringWriter stackTraceContent = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stackTraceContent);
        e.printStackTrace(printWriter);
        trace(source, EventType.ERROR,"\t".concat(stackTraceContent.toString()));
    }

    @Override
    public void logInfo(Class<?> source, String... event) {
        String s = String.join(" ",event);
        trace(source, EventType.INFO, s);
    }

    public void trace(Class<?> source, EventType t, String event) {
        lineNumber++;
        LocalDateTime currentTime = LocalDateTime.now();
        logStorage.append(String.format(template, lineNumber, t, dtf.format(currentTime), source, event));
        logStorage.append(System.lineSeparator());
    }

    @Override
    public String getLog() {
        return logStorage.toString();
    }
}
