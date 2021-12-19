package contracts.businesslogic;

import enums.EventType;

public interface DiagnosticLogger {
    void logError(Class<?> source, String... eventData);
    void logInfo(Class<?> source, String... eventData);
    String getLog();
}
