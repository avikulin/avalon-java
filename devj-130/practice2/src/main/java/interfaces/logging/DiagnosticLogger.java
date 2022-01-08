package interfaces.logging;

import java.awt.*;

public interface DiagnosticLogger {
    void logError(Class<?> source, String... eventData);
    void logError(Class<?> source, Exception e, String... eventData);
    void logInfo(Class<?> source, String... eventData);
    String getLog();
}
