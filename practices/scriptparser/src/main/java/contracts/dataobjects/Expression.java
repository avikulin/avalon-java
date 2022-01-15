package contracts.dataobjects;

import enums.FunctionType;
import enums.ExpressionNodeType;
import enums.OperationType;

public interface Expression {
    ExpressionNodeType getType();
    Double getNumericValue();
    String getStringValue();
    void setNumericValue(double value);
    void setStringValue(String value);
    OperationType getOperation();
    FunctionType getFunction();
    void setFunction(FunctionType func);
    Expression getLeft();
    Expression getRight();
}
