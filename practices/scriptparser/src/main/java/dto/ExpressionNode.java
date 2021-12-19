package dto;

import contracts.dataobjects.Expression;
import enums.FunctionType;
import enums.NodeType;
import enums.OperationType;

import java.text.DecimalFormat;

public class ExpressionNode implements Expression {
    private NodeType type;
    private double numericValue;
    private String stringValue;
    private OperationType operator;
    private FunctionType func;
    private Expression left;
    private Expression right;

    /**
     * Constructor for a scalar leaf-node
     * @param value
     */
    public ExpressionNode(Double value){
        setType(NodeType.NUMBER_LITERAL);
        setOperator(OperationType.NOT_AN_OPERATION);
        setNumericValue(value);
    }

    /**
     * Constructor for a scalar leaf-node
     * @param value
     */
    public ExpressionNode(String value){
        setType(NodeType.STRING_LITERAL);
        setOperator(OperationType.NOT_AN_OPERATION);
        setStringValue(value);
    }

    public ExpressionNode(NodeType type, OperationType operator) throws NullPointerException {
        setType(type);
        setOperator(operator);
    }

    public ExpressionNode(NodeType type, OperationType operator, Expression left, Expression right) throws NullPointerException {
        setType(type);
        setOperator(operator);
        setLeft(left);
        setRight(right);
    }

    public void setType(NodeType type) throws NullPointerException {
        if (type == null){
            throw new NullPointerException("Type param reference must be set");
        }
        this.type = type;
    }

    public void setOperator(OperationType operator) throws NullPointerException {
        if (operator == null){
            throw new NullPointerException("Operation type param reference must be set");
        }

        if ((operator != OperationType.NOT_AN_OPERATION)&&(type!= NodeType.EXPRESSION)){
            throw new IllegalStateException(
                    String.format("This node type (%s) is incompatible with operation type provided (%s)",
                            type, operator));
        }
        this.operator = operator;
    }

    public void setNumericValue(double numericValue) {
        this.numericValue = numericValue;
    }

    public void setStringValue(String stringValue) { //принимаем null - может потребоваться при смене типа узла
        this.stringValue = stringValue;
    }

    public FunctionType getFunction() {
        if (type!=NodeType.FUNCTION){
            throw new IllegalStateException("Current node is not a function node");
        }
        return func;
    }

    public void setFunction(FunctionType func) {
        if (type!=NodeType.FUNCTION){
            throw new IllegalStateException("Current node is not a function node");
        }
        this.func = func;
    }

    private void setLeft(Expression left) throws NullPointerException {
        if (left == null){
            throw new NullPointerException("Left node reference must be set");
        }
        this.left = left;
    }

    private void setRight(Expression right) throws NullPointerException {
        if (operator!= OperationType.NOT_AN_OPERATION &&right == null){
            throw new NullPointerException("Right node reference must be set");
        }
        this.right = right;
    }

    @Override
    public NodeType getType() {
        return type;
    }

    @Override
    public Double getNumericValue() {
        if (type != NodeType.NUMBER_LITERAL){
            throw new IllegalStateException("Node does not contain numeric value");
        }
        return numericValue;
    }

    @Override
    public String getStringValue() {
        if (type == NodeType.NUMBER_LITERAL){
            throw new IllegalStateException("Node does not contain numeric value");
        }
        return stringValue;
    }

    @Override
    public OperationType getOperation() {
        return operator;
    }

    @Override
    public Expression getLeft() {
        return left;
    }

    @Override
    public Expression getRight() {
        return right;
    }

    @Override
    public String toString() {
        switch (type){
            case NUMBER_LITERAL: return new DecimalFormat("#.0#").format(numericValue);
            case STRING_LITERAL:
            case VARIABLE: return stringValue;
            case FUNCTION: return func.toString().concat("(").concat(this.left.toString()).concat(")");
            case EXPRESSION: return String.format("(%s)%s(%s)",
                    this.left.toString(), this.operator.toString(), this.right.toString());
        }
        throw new IllegalStateException("Incorrect node type detected");
    }
}
