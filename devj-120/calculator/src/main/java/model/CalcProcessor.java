package model;

import enums.CalculationMode;
import enums.Operation;
import enums.OperationType;
import interfaces.CalculationModel;
import utils.Tracer;
import utils.ValueConverter;

import java.math.BigInteger;
import java.util.*;

import static constants.Constants.ZERO_VALUE;

public class CalcProcessor implements CalculationModel {
    CalculationMode mode;
    StringJoiner calculationLog;
    Deque<BigInteger> valueRegister;
    Deque<Operation> operationRegister;
    int operationCounter;

    public CalcProcessor(CalculationMode mode)  throws IllegalArgumentException {
        if (mode == null){
            String msg = "Calculation mode reference param must be not null";
            IllegalArgumentException exception = new IllegalArgumentException(msg);
            Tracer.get().logError(this.getClass(),exception, "Constructor error");
            throw exception;
        }

        this.mode = mode;
        this.valueRegister = new ArrayDeque<>();
        this.operationRegister = new ArrayDeque<>();
        this.calculationLog = new StringJoiner("\n");
        this.operationCounter = 0;

        Tracer.get().logInfo(this.getClass(), "Instance created");
    }

    @Override
    public void setMode(CalculationMode mode) throws IllegalArgumentException {
        if (mode == null) {
            String msg = "Mode reference param must be not null";
            IllegalArgumentException exception = new IllegalArgumentException(msg);
            Tracer.get().logError(this.getClass(),exception, "Error in changing calculation mode");
            throw exception;
        }
        this.mode = mode;
        Tracer.get().logInfo(this.getClass(), "Calculation mode changed to ", this.mode.toString());
    }

    private void addLogEntry(String record){
        operationCounter++;
        calculationLog.add(String.format("%4d) %s", operationCounter, record));
    }

    public String processCalculation() {
        Operation operation = operationRegister.peek();

        // если нажали "=" без ввода аргументов или ввели знак бинарной операции
        if (operation == null || (operation.getType()==OperationType.BINARY && valueRegister.size() == 1)) {
            Tracer.get().logInfo(this.getClass(), "Empty context calculation. Returning identity value.");
            return ValueConverter.toString(valueRegister.peek(), mode); // контекст вычислений остается без изменений
        }

        // если нажата кнопка унарной операции
        if (operation.getType() == OperationType.UNARY && !valueRegister.isEmpty()) {
            Tracer.get().logInfo(this.getClass(), "Unary operation calculation", operation.toString());
            try {
                operationRegister.pop(); // забираем операцию на исполнение
                BigInteger operand = valueRegister.pop();
                BigInteger result = operation.getFunction().apply(operand, new BigInteger(ZERO_VALUE));
                String strOperand = String.format("%s %s", mode, ValueConverter.toString(operand, mode));
                String strOperation = operation.toString(strOperand);
                String strResult = ValueConverter.toString(result, mode);
                String logEntry = String.format("CALCULATION: %s = %s", strOperation, strResult);
                addLogEntry(logEntry);
                return strResult;
            } catch (ArithmeticException | NoSuchElementException exception) {
                String msg = "Calculation error: ".concat(exception.getMessage());
                IllegalStateException ex = new IllegalStateException(msg);
                Tracer.get().logError(this.getClass(),ex, "Unary function calculation error");
                throw ex;
            }
        }

        // если нажата кнопка бинарной операции
        if (operation.getType() == OperationType.BINARY && valueRegister.size() == 2) {
            Tracer.get().logInfo(this.getClass(), "Binary operation calculation", operation.toString());
            try {
                operationRegister.pop(); // забираем операцию на исполнение
                BigInteger operandB = valueRegister.pop();
                BigInteger operandA = valueRegister.pop();

                if (operandB.equals(new BigInteger(ZERO_VALUE))&&operation==Operation.DIV){
                    throw new ArithmeticException("Division by zero is prohibited");
                }

                BigInteger result = operation.getFunction().apply(operandA, operandB);
                valueRegister.push(result); // возвращаем результат в стэк для последующего использования
                String strResult = ValueConverter.toString(result, mode);
                String strOperandA = String.format("%s %s", mode, ValueConverter.toString(operandA, mode));
                String strOperandB = String.format("%s %s", mode, ValueConverter.toString(operandB, mode));
                String strLogResult = String.format("%s %s", mode, ValueConverter.toString(result, mode));

                String strOperation = operation.toString(strOperandA, strOperandB);
                String logEntry = String.format("CALCULATION: %s = %s", strOperation, strLogResult);
                addLogEntry(logEntry);
                return strResult;
            } catch (ArithmeticException | NoSuchElementException exception) {
                String msg = "Calculation error: ".concat(exception.getMessage());
                IllegalStateException ex = new IllegalStateException(msg);
                Tracer.get().logError(this.getClass(),ex, "Binary function calculation error");
                throw ex;
            }
        }

        // ошибочная ситуация - не хватает операндов не для унарной, ни для бинарной операции
        String msg = "Operation can't be performed without operands";
        IllegalStateException exception = new IllegalStateException(msg);
        Tracer.get().logError(this.getClass(),exception, "Unexpected calculation context state");
        throw exception;
    }

    @Override
    public void enterOperand(String strValue) throws IllegalArgumentException {
        Tracer.get().logInfo(this.getClass(), "Register operand", strValue);

        if (strValue == null || strValue.isEmpty()) {
            String msg = "Value reference param must be not null and non-empty string";
            IllegalArgumentException exception = new IllegalArgumentException(msg);
            Tracer.get().logError(this.getClass(),exception, "Error in registering operand");
            throw exception;
        }

        if (mode == null) {
            String msg = "Calculation mode must be set";
            IllegalStateException exception = new IllegalStateException(msg);
            Tracer.get().logError(this.getClass(),exception, "Error in registering operand");
            throw exception;
        }

        BigInteger value = ValueConverter.fromString(strValue, mode);

        // если в регистре несколько операндов без операции - то оставляем последний
        if (this.operationRegister.isEmpty() && !this.valueRegister.isEmpty()) {
            valueRegister.clear();
        }
        valueRegister.push(value);
        addLogEntry(String.format("OPERAND ENTERED: %s %s", mode, strValue));
    }

    @Override
    public String enterOperation(Operation operation) throws IllegalStateException {
        if (operation == null) {
            String msg = "Operation reference param must be not null";
            IllegalArgumentException exception = new IllegalArgumentException(msg);
            Tracer.get().logError(this.getClass(),exception, "Error in registering operation");
            throw exception;
        }
        Tracer.get().logInfo(this.getClass(), "Register operation", operation.toString());
        addLogEntry(String.format("OPERATION ENTERED: %s ", operation.toString()));

        operationRegister.push(operation);
        return processCalculation();
    }

    @Override
    public void resetState() {
        Tracer.get().logInfo(this.getClass(), "Clear state & re-init calculation context");
        operationRegister.clear();
        valueRegister.clear();
        valueRegister.push(new BigInteger(ZERO_VALUE));
        addLogEntry("CLEAR STATE");
        addLogEntry("ENTER DEFAULT OPERAND: 0");
    }

    @Override
    public String getHistory() {
        return calculationLog.toString();
    }
}
