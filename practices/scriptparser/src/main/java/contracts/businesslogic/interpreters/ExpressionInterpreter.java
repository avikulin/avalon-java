package contracts.businesslogic.interpreters;

import contracts.dataobjects.Expression;

public interface ExpressionInterpreter {
    double interpretExpression(Expression expression);
}
