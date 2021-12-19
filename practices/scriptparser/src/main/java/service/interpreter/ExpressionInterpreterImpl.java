package service.interpreter;

import contracts.dataobjects.Expression;
import enums.FunctionType;
import enums.NodeType;
import enums.OperationType;

import java.util.Map;

public class ExpressionInterpreterImpl implements contracts.businesslogic.interpreters.ExpressionInterpreter {
    private Map<String, Double> variableContext;

    public ExpressionInterpreterImpl(Map<String, Double> variableContext) {
        if (variableContext == null) {
            throw new NullPointerException("Variable context object param must be set");
        }
        this.variableContext = variableContext;
    }

    private double interpretStringLiteral(Expression expression) {
        throw new UnsupportedOperationException("String literals aren't supported in this grammar");
    }

    private double interpretNumberLiteral(Expression expression) {
        return expression.getNumericValue();
    }

    private double interpretVariable(Expression expression) {
        String variableName = expression.getStringValue();
        Double value = variableContext.get(variableName);
        if (value == null) {
            String msg = "Variable haven't been previously set: ".concat(variableName);
            throw new IllegalStateException(msg);
        }
        return value;
    }

    private double interpretFunction(Expression expression) {
        FunctionType fun = expression.getFunction();
        Expression subExpression = expression.getLeft();
        try {
            double funArgumentValue = interpretExpression(subExpression);
            return fun.getDelegate().apply(funArgumentValue);
        } catch (ArithmeticException | IllegalStateException ex) {
            String msg = String.format("Error in calculation: %s(%s)", fun.toString(), subExpression.toString());
            throw new ArithmeticException(msg);
        }
    }

    private double interpretSubExpression(Expression expression) {
        OperationType operationType = expression.getOperation();
        Expression leftPart = expression.getLeft();
        Expression rightPart = expression.getRight();
        try {
            double lvalue = interpretExpression(leftPart);
            double rvalue = interpretExpression(rightPart);
            switch (operationType) {
                case SUM:
                    return lvalue + rvalue;
                case SUB:
                    return lvalue - rvalue;
                case MUL:
                    return lvalue * rvalue;
                case DIV:
                    return lvalue / rvalue; // тут если что поймаем ArithmeticException в блоке catch(..)
                case NOT_AN_OPERATION:
                default: {
                    String msg = "Incorrect operation type for such type of expression: ".concat(expression.toString());
                    throw new IllegalStateException(msg);
                }
            }
        } catch (ArithmeticException | IllegalStateException ex) {
            String msg = String.format("Error in calculation: %s", expression.toString());
            throw new ArithmeticException(msg);
        }
    }

    @Override
    public double interpretExpression(Expression expression) {
        if (expression == null) {
            String msg = "Expression object param must be set";
            throw new IllegalArgumentException(msg);
        }
        NodeType type = expression.getType();
        switch (type) {
            case NUMBER_LITERAL:
                return interpretNumberLiteral(expression);
            case STRING_LITERAL:
                return interpretStringLiteral(expression);
            case VARIABLE:
                return interpretVariable(expression);
            case FUNCTION:
                return interpretFunction(expression);
            case EXPRESSION:
                return interpretSubExpression(expression);
            default:
                return 0;
        }
    }
}
