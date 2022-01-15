package utils;

import contracts.DiagnosticLogger;
import enums.EventType;

import java.io.*;
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

    @Override
    public void saveToFile(String fileName) throws IOException {
        if (fileName == null || fileName.isEmpty()){
            String msg = "File name should not be null or empty string";
            trace(this.getClass(), EventType.ERROR, msg);
            throw new IllegalArgumentException(msg);
        }

        File logFile = new File(fileName);
        if (logFile.exists() || logFile.isDirectory()) {
            String msg = String.format("Invalid filename passed: %s. " +
                    "Filename must be unique and should not correspond any directory name", fileName);
            trace(this.getClass(), EventType.ERROR, msg);
            throw new IllegalArgumentException(msg);
        }
        if (logStorage.length()==0){
            return;
        }

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(logFile))){
            writer.write(logStorage.toString());
        };
    }
}
