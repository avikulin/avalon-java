package interfaces;

import enums.CalculationMode;
import enums.Operation;

public interface CalculationModel {
    void setMode(CalculationMode mode) throws IllegalArgumentException ;
    void enterOperand(String value) throws IllegalArgumentException;
    String enterOperation(Operation operation) throws IllegalStateException;
    void resetState();
    String getHistory();
}
