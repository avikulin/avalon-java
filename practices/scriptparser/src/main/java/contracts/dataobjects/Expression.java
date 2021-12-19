package contracts.dataobjects;

import enums.FunctionType;
import enums.NodeType;
import enums.OperationType;

public interface Expression {
    NodeType getType();
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
