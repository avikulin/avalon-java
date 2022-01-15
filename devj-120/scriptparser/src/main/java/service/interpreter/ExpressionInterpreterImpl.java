package service.interpreter;

import contracts.businesslogic.utils.DiagnosticLogger;
import contracts.dataobjects.Expression;
import enums.ExpressionNodeType;
import enums.FunctionType;
import enums.OperationType;
import utils.Tracer;

import java.util.Map;

public class ExpressionInterpreterImpl implements contracts.businesslogic.interpreters.ExpressionInterpreter {
    private Map<String, Double> variableContext;
    private DiagnosticLogger logger;

    public ExpressionInterpreterImpl() {
        logger = Tracer.get();
    }


    private double interpretStringLiteral(Expression expression) {
        String msg = "String literals aren't supported in this grammar";
        logger.logError(this.getClass(), msg);
        throw new UnsupportedOperationException(msg);
    }

    private double interpretNumberLiteral(Expression expression) {
        return expression.getNumericValue();
    }

    private double interpretVariable(Expression expression) {
        if (variableContext == null) {
            String msg = "Variable context haven't been properly set";
            logger.logError(this.getClass(), msg);
            throw new UnsupportedOperationException(msg);
        }

        String variableName = expression.getStringValue();
        Double value = variableContext.get(variableName);
        if (value == null) {
            String msg = "Variable haven't been previously set: ".concat(variableName);
            logger.logError(this.getClass(), msg);
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
            logger.logError(this.getClass(), ex, msg);
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
            return operationType.getDelegate().apply(lvalue, rvalue);
        } catch (ArithmeticException | IllegalStateException ex) {
            String msg = String.format("Error in calculation: %s", expression.toString());
            logger.logError(this.getClass(), ex, msg);
            throw new ArithmeticException(msg);
        }
    }

    @Override
    public void registerVariableContext(Map<String, Double> variableContext) {
        if (variableContext == null) {
            String msg = "Variable context object param must be set";
            logger.logError(this.getClass(), msg);
            throw new NullPointerException(msg);
        }
        this.variableContext = variableContext;
    }

    @Override
    public double interpretExpression(Expression expression) {
        if (expression == null) {
            String msg = "Expression object param must be set";
            logger.logError(this.getClass(), msg);
            throw new IllegalArgumentException(msg);
        }
        ExpressionNodeType type = expression.getType();
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
