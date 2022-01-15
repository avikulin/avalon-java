package contracts.businesslogic.fabrics;

import contracts.dataobjects.Expression;
import enums.FunctionType;
import enums.OperationType;

public interface ExpressionFabric {
    Expression createValueNode(double value);

    Expression createLiteralNode(String value);

    Expression createVariableNode(String variableId);

    Expression createExpressionNode(OperationType type, Expression arg1, Expression arg2);

    Expression createFunctionNode(FunctionType func, Expression argumentExpr);
}
