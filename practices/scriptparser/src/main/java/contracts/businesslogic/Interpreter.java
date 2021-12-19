package contracts.businesslogic;

import contracts.dataobjects.Expression;

import java.util.HashMap;

public interface Interpreter {
    void setVariableContext(HashMap<String, Double> context);
    void setResultContext(StringBuilder context);
    void setLogContext(DiagnosticLogger context);
    void interpret(Expression expression);
}
