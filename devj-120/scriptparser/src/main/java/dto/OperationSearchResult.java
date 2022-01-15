package dto;

public class OperationSearchResult {
    private int operationPos;
    private char operationSymbol;

    public OperationSearchResult(int operationPos, char operationSymbol) {
        this.operationPos = operationPos;
        this.operationSymbol = operationSymbol;
    }

    public int getOperationPos() {
        return operationPos;
    }

    public char getOperationSymbol() {
        return operationSymbol;
    }
}
