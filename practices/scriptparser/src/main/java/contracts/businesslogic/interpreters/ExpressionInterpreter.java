package contracts.businesslogic.interpreters;

import contracts.dataobjects.Expression;

import java.util.Map;

public interface ExpressionInterpreter {
    void registerVariableContext(Map<String, Double> variableContext);

    double interpretExpression(Expression expression);
}
