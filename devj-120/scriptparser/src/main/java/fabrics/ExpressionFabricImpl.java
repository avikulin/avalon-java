package fabrics;

import contracts.businesslogic.fabrics.ExpressionFabric;
import contracts.dataobjects.Expression;
import dto.ExpressionNode;
import enums.FunctionType;
import enums.ExpressionNodeType;
import enums.OperationType;

public class ExpressionFabricImpl implements ExpressionFabric {
    public Expression createValueNode(double value) {
        return new ExpressionNode(value);
    }

    public Expression createLiteralNode(String value) {
        return new ExpressionNode(value);
    }

    public Expression createVariableNode(String variableId) {
        Expression node = new ExpressionNode(ExpressionNodeType.VARIABLE, OperationType.NOT_AN_OPERATION);
        node.setStringValue(variableId);
        return node;
    }

    public Expression createExpressionNode(OperationType type, Expression arg1, Expression arg2) {
        return new ExpressionNode(ExpressionNodeType.EXPRESSION, type, arg1, arg2);
    }

    public Expression createFunctionNode(FunctionType func, Expression argumentExpr) {
        Expression node = new ExpressionNode(ExpressionNodeType.FUNCTION, OperationType.NOT_AN_OPERATION, argumentExpr, null);
        node.setFunction(func);
        return node;
    }
}
