package utils;

import contracts.businesslogic.utils.DiagnosticLogger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class ConsoleHelper {
    public static void printToConsole(BufferedWriter outputContext, String s) {
        try {
            outputContext.write(s);
            outputContext.flush();
        } catch (IOException ioException) {
            DiagnosticLogger logger = Tracer.get();
            logger.logError(ConsoleHelper.class, ioException, "Unexpected IO-error happened");
        }
    }

    public static String readFromConsole(BufferedReader inputContext) {
        String res = "";
        try {
            res = inputContext.readLine();
        } catch (IOException ioException) {
            DiagnosticLogger logger = Tracer.get();
            logger.logError(ConsoleHelper.class, ioException, "Unexpected IO-error happened");
        }
        return res;
    }
}
